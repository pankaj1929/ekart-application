package com.ekart;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EkartConfig {
    
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
