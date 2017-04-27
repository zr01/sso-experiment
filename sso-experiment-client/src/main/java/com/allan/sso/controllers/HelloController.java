package com.allan.sso.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@PreAuthorize("#oauth2.hasScope('read')")
	@RequestMapping("/hello")
	public Map<String, String> hello(){
		Map<String, String> resp = new HashMap<String, String>();
		resp.put("Response", "Hello!");
		return resp;
	}

	@PreAuthorize("#oauth2.hasRole('ROLE_ADMIN'")
	@RequestMapping("/hello/admin")
	public Map<String, String> helloAdmin(){
		Map<String, String> resp = new HashMap<String, String>();
		resp.put("Response", "Hello admin!");
		return resp;
	}
}