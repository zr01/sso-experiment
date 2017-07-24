package com.allan.sso.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class JWTController {

	static final Logger l = LoggerFactory.getLogger(JWTController.class);
	
	@RequestMapping("/sso/callback")
	public String retrieveTokenAndRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//See if we got the code
		l.info("We got the code: {}", request.getParameter("code"));
		
		//Let's see if we can get the token
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		//client_secret=sso-experiment-client-secret&client_id=sso-experiment-client&
		String callbackString = "code=${code}&grant_type=authorization_code&redirect_uri=http://localhost:8081/sso/callback".replace("${code}", request.getParameter("code"));
		
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//		headers.add("Authorization", "Basic " + Base64Utils.encodeToString("sso-experiment-client:sso-experiment-client-secret".getBytes()));
//		headers.add("Authorization", "Basic " + Base64Utils.encodeToString("sso:pw".getBytes()));
		headers.add("X-Requested-With", "XMLHttpRequest");
		headers.add("Authorization", "Basic " + Base64Utils.encodeToString("sso-experiment-client:sso-experiment-client-secret".getBytes()));
		
		HttpEntity<String> entity = new HttpEntity<String>(callbackString, headers);
		
		try{
			ResponseEntity<String> resp = restTemplate.exchange("http://localhost:8080/oauth/token", HttpMethod.POST, entity, String.class);
			l.info("oauth/token Body: {}", resp.getBody());
			l.info("oauth/token Headers: {}", resp.getHeaders().toString());
			
			//Redirect when successful
			//response.sendRedirect("/hello");
//			Map<String, String> token = new HashMap<>();
//			token.put("jwt", resp.getBody());
			
			
			return resp.getBody();
		}catch(RestClientException e){
			l.error("Error: {}", e.getLocalizedMessage());
			l.error("Exception: ", e);
			
			throw e;
		}
	}
	
	@ExceptionHandler(RestClientException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, String> handleRestClientException(RestClientException e){
		Map<String, String> map = new HashMap<>();
		map.put("Cause", "Unable to get Token");
		return map;
	}
}
