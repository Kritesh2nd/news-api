package com.exm.news.security.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.exm.news.security.authentication.UserAuth;
import com.exm.news.security.provider.BasicAuthProvider;
import com.exm.news.security.provider.TokenAuthProvider;

@Component
public class AuthManager implements AuthenticationManager{

	@Autowired
	private BasicAuthProvider basicAuthProvider;
	
	@Autowired
	private TokenAuthProvider tokenAuthProvider;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		UserAuth userAuth = (UserAuth) authentication;
		
		if(userAuth.getToken() == null && basicAuthProvider.supports(authentication.getClass())) {
			return basicAuthProvider.authenticate(authentication);
		}
		else if(userAuth.getToken() != null && tokenAuthProvider.supports(authentication.getClass())) {
			return tokenAuthProvider.authenticate(authentication);
		}
		
		throw new BadCredentialsException("Bad Credentials Exception");
	}

}
