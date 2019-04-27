package io.quarkus.agroal.runtime.vault;

import java.io.*;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.agroal.api.security.SimplePassword;

/**
 * 
 */
public class VaultPassword extends SimplePassword {

    private static final Logger log = Logger.getLogger(VaultPassword.class.getName());

    public static final String APPLICATION_JSON = "application/json";
    public static final Charset DEFAULT_CHARSET = null;
    public static final String X_VAULT_TOKEN = "X-Vault-Token";

    private ObjectMapper objectMapper = new ObjectMapper();

    private AtomicReference<VaultLease> auth = new AtomicReference<>(null);

    private VaultRuntimeConfig config;

    public VaultPassword(VaultRuntimeConfig config) {
        super("vault");
        this.config = config;
    }

    @Override
    public Properties asProperties() {
        Properties properties = new Properties();
        properties.put("password", readPasswordFromVault());
        log.info("using password properties: " + properties); // FIXME confidential
        return properties;
    }

    private String readPasswordFromVault() {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            String token;
            Optional<String> tokenOpt = config.token;

            if (tokenOpt.isPresent()) {
                token = tokenOpt.get();
            } else {
                VaultLease lease = auth.get();
                if (lease == null || lease.isExpired()) {
                    log.info("k8s login");
                    String jwt = readJwt(config.jwtPath);
                    log.info("read jwt from: " + config.jwtPath + " => " + jwt); // FIXME confidential
                    VaultLeaseResult result = login(httpclient, config.url, jwt, config.role.get());
                    token = replaceLease(result.lease);
                } else if (lease.shouldRenew(config.renewGracePeriod)) {
                    log.info("renewing lease");
                    VaultLeaseResult result = renewSelf(httpclient, config.url, lease.clientToken);
                    token = replaceLease(result.lease);
                } else {
                    log.info("reusing current lease good until " + lease.getExpiredDate());
                    token = lease.clientToken;
                }
            }

            log.info("reading secret");
            VaultSecretResult secret = getSecret(httpclient, config.url, token, config.secretPath);
            log.info("secret = " + secret); // FIXME confidential
            String passwordValue = secret.data.get(config.secretKey);

            return passwordValue;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private VaultLeaseResult login(CloseableHttpClient httpclient, String url, String jwt, String role) throws IOException {
        HttpPost httpPost = new HttpPost(url + "/v1/auth/kubernetes/login");
        BasicHttpEntity entity = new BasicHttpEntity();
        ObjectNode content = objectMapper.createObjectNode().put("role", role).put("jwt", jwt);
        entity.setContent(new ByteArrayInputStream(objectMapper.writeValueAsBytes(content)));
        entity.setContentEncoding(JsonEncoding.UTF8.getJavaName());
        httpPost.setEntity(entity);

        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            log.info(response.getStatusLine());
            assertOK(response, httpPost);
            return readObject(response, VaultLeaseResult.class);
        }
    }

    private VaultLeaseResult renewSelf(CloseableHttpClient httpclient, String url, String token) throws IOException {
        HttpPost httpPost = new HttpPost(url + "/v1/auth/token/renew-self");
        httpPost.setHeader(X_VAULT_TOKEN, token);

        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            log.info(response.getStatusLine());
            assertOK(response, httpPost);
            return readObject(response, VaultLeaseResult.class);
        }
    }

    private VaultSecretResult getSecret(CloseableHttpClient httpclient, String url, String token, String path)
            throws IOException {
        HttpGet httpGet = new HttpGet(url + "/v1/secret/" + path);
        httpGet.setHeader(X_VAULT_TOKEN, token);
        return get(httpclient, httpGet, VaultSecretResult.class);
    }

    private VaultSealStatus getSealStatus(CloseableHttpClient httpclient, String url) throws IOException {
        HttpGet httpGet = new HttpGet(url + "/v1/sys/seal-status");
        return get(httpclient, httpGet, VaultSealStatus.class);
    }

    // ------

    private String replaceLease(VaultLease lease) {
        log.info("new lease = " + lease.getConfidentialInfo()); // FIXME confidential
        auth.set(lease);
        return lease.clientToken;
    }

    private String readJwt(String path) throws IOException {
        try (FileInputStream in = new FileInputStream(path)) {
            byte[] buf = new byte[1024];
            int c;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((c = in.read(buf)) > 0) {
                out.write(buf, 0, c);
            }
            return new String(out.toByteArray());
        }
    }

    private <T> T get(CloseableHttpClient httpclient, HttpGet httpGet, Class<T> clazz) throws IOException {
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            log.debug(response.getStatusLine());
            assertOK(response, httpGet);
            return readObject(response, clazz);
        }
    }

    private void assertOK(CloseableHttpResponse response, Object action) {
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException(action + "status code error " + response.getStatusLine().getStatusCode());
        }
    }

    private <T> T readObject(CloseableHttpResponse response, Class<T> clazz) throws IOException {
        HttpEntity entity = response.getEntity();
        try {
            if (isApplicationJson(entity)) {
                String json = EntityUtils.toString(entity, DEFAULT_CHARSET);
                return (T) objectMapper.readValue(json, clazz);
            } else {
                throw new RuntimeException(
                        "unexpected content type " + entity.getContentType() + "; " + response.getStatusLine());
            }
        } finally {
            EntityUtils.consume(entity);
        }
    }

    private boolean isApplicationJson(HttpEntity entity) {
        Header contentType = entity.getContentType();
        if (contentType == null) {
            return false;
        } else {
            HeaderElement[] elements = contentType.getElements();
            return Arrays.stream(elements).anyMatch(he -> APPLICATION_JSON.equals(he.getName()));
        }
    }

    private JsonNode get(CloseableHttpClient httpclient, HttpGet httpGet) throws IOException {
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            log.debug(response.getStatusLine());
            assertOK(response, httpGet);
            return readTree(response);
        }
    }

    private JsonNode readTree(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        try {
            if (isApplicationJson(entity)) {
                String json = EntityUtils.toString(entity, DEFAULT_CHARSET);
                return objectMapper.readTree(json);
            } else {
                throw new RuntimeException(
                        "unexpected content type " + entity.getContentType() + "; " + response.getStatusLine());
            }
        } finally {
            EntityUtils.consume(entity);
        }
    }

}
