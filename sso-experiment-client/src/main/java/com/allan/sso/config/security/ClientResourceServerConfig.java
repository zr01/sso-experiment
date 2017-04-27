package com.allan.sso.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ClientResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Value("${jwt.signing.key}")
	private String signingKey;
	
	@Value("${resource.id:sso-experiment-client-site}")
	private String resourceId;

	@Override
	public void configure(ResourceServerSecurityConfigurer config){
		config
			.resourceId(resourceId)
			.tokenServices(tokenServices())
		;
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter(){
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(signingKey);
		return converter;
	}
	
	@Bean
	public TokenStore tokenStore(){
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Bean
	@Primary
	public DefaultTokenServices tokenServices(){
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(tokenStore());
		return tokenServices;
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception{
		http
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		.and()
			.authorizeRequests()
				.antMatchers("/sso/callback").permitAll()
			.anyRequest().authenticated();
	}
}
