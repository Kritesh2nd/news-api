package com.exm.news.security.provider;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import com.exm.news.entity.auth.Login;
import com.exm.news.repository.auth.LoginRepository;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.exm.news.entity.app.Authority;
import com.exm.news.entity.app.User;
import com.exm.news.repository.app.UserRepository;
import com.exm.news.service.JwtService;
import com.exm.news.security.authentication.UserAuth;

import io.jsonwebtoken.security.SignatureException;

@Component
public class TokenAuthProvider implements AuthenticationProvider{
	
	@Autowired
	private UserRepository userRespository;

	@Autowired
	private LoginRepository loginRespository;
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		UserAuth userTokenAuth = (UserAuth) authentication;
		Login login  = loginRespository.findLoginByEmail(userTokenAuth.getEmail());

		if(login == null) {
			throw new UsernameNotFoundException("user of given token not found");
		}
		if(userTokenAuth.getToken() == null) {
			throw new UsernameNotFoundException("token not found");
		}
		
		if(jwtService.isTokenExpired(userTokenAuth.getToken())) {
			throw new UsernameNotFoundException("token expired");
		}

		User user  = userRespository.findUserById(login.getId());

		try {
			if(jwtService.isTokenValid(userTokenAuth.getToken(), userTokenAuth)){
				System.out.println("valid token");
				System.out.println("user: "+user.toString());
				Set<GrantedAuthority> authoritySet = new HashSet<GrantedAuthority>();

				for(Authority auth : user.getAuthorities() ) {
					authoritySet.add(new SimpleGrantedAuthority(auth.getName()));
				}
				List<GrantedAuthority> authorityList = new ArrayList<>(authoritySet);
				
				return new UserAuth(true, login.getEmail(), null, null, authorityList);
			}
		}
		catch(SignatureException e) {			
			throw new SignatureException("SignatureException");
		}
		return null;
	}

	
	@Override
	public boolean supports(Class<?> authentication) {
		return UserAuth.class.equals(authentication);
	}

}
