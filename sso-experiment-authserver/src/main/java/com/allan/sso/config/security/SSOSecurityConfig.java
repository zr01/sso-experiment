package com.allan.sso.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SSOSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	protected void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception{
		//@formatter:off
		auth
			.inMemoryAuthentication()
			.withUser("admin").password("adm1n").roles("ADMIN")
		.and()
			.withUser("aatest").password("testera").roles("USER");
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
