package com.target.pdp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;

/**
 * This class configures the security for v2 version API.
 * 
 * @author KK
 *
 */

@EnableResourceServer
@Configuration
public class ResourceServerSecurityConfig extends ResourceServerConfigurerAdapter {
	
	@Value("${okta.oauth2.keySetUri}")
	String keySetURI;
	
	@Value("${okta.oauth2.authserverId}")
	String authServerId;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		JwkTokenStore tokenStore = new JwkTokenStore(keySetURI);
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore);
		resources.resourceId(authServerId);
		resources.tokenServices(defaultTokenServices);
		resources.tokenStore(tokenStore);
	}
	
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/v2/**").authorizeRequests().anyRequest().authenticated();
	}

}
