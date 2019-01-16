package com.raffa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecretController {
	
	private Log log = LogFactory.getLog(SecretController.class);

	@Value("${password}")
	String password;

	@RequestMapping("/secret")
	public String secret() {
		return "my secret is" + password;
	}
}
