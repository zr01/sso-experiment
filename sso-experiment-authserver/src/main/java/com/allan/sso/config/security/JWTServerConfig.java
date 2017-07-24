package com.allan.sso.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class JWTServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Value("${jwt.signing.key}")
	private String signingKey;
	
	@Value("${resource.id:sso-experiment-client-site}")
	private String resourceId;
	
	@Autowired
	@Qualifier("authenticationManagerBean")
	AuthenticationManager authenticationManager;

	@Bean
	public TokenStore tokenStore(){
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter(){
		JwtAccessTokenConverter jatc = new JwtAccessTokenConverter();
		//Parameterize the signing key
		jatc.setSigningKey(signingKey);
		return jatc;
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception{
		endpoints
			.tokenStore(tokenStore())
			.accessTokenConverter(accessTokenConverter())
			.authenticationManager(authenticationManager);
		;
	}
	
	@Bean
	@Primary
	public DefaultTokenServices tokenServices(){
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		
		tokenServices.setTokenStore(tokenStore());
		tokenServices.setSupportRefreshToken(true);//parameterize
		
		return tokenServices;
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer
			.allowFormAuthenticationForClients()
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()");
		;
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception{
		clients
			.inMemory()
				.withClient("sso-experiment-client") //Should be setup externally
					.authorizedGrantTypes("authorization_code", "implicit", "refresh_token")
					.authorities("ROLE_CLIENT", "ROLE_ADMIN")
					.scopes("read", "write")
					.resourceIds(resourceId)
					.accessTokenValiditySeconds(300)
					.autoApprove(true)
					.secret("sso-experiment-client-secret") //Should be setup externally
		;
	}
}
