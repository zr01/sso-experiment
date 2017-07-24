package com.allan.sso.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SSOSecurityConfig extends WebSecurityConfigurerAdapter {
    
    static final Logger l = LoggerFactory.getLogger(SSOSecurityConfig.class);

    @Autowired
    AuthenticationProvider staticAuthenticationProvider;
    
    @Autowired
    AuthenticationProvider legacyAuthenticationProvider;
    
	@Autowired
	protected void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception{
	    AuthenticationException ae = null;
	    
		//@formatter:off
	    try {
	        auth.authenticationProvider(staticAuthenticationProvider);
	    } catch (AuthenticationException e) {
	        //Primary failed, proceed to secondary authentication
	        l.error("Primary authentication failed.");
	        ae = e;
	    }
	    
	    try {
	        auth.authenticationProvider(legacyAuthenticationProvider);
	    } catch (AuthenticationException e) {
	        l.error("Secondary authentication failed.");
	        ae = ae != null ? ae : e;
	    }
//		auth
//			.inMemoryAuthentication()
//			.withUser("admin").password("adm1n").roles("ADMIN")
//		.and()
//			.withUser("aatest").password("testera").roles("USER");
		//@formatter:on
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
			.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS).permitAll()
				.anyRequest().authenticated()
				.antMatchers("/oauth/authorize", "/oauth/token").permitAll()
				.anyRequest().authenticated()
		.and()
			.httpBasic()
		.and()
			.csrf().disable();
		;
	}
}
