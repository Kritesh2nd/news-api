package com.exm.news.security.provider;

import com.exm.news.entity.auth.Login;
import com.exm.news.repository.auth.LoginRepository;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.AuthenticationProvider;

import com.exm.news.entity.app.Authority;
import com.exm.news.entity.app.User;
import com.exm.news.repository.app.UserRepository;
import com.exm.news.security.authentication.UserAuth;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

@Component
public class BasicAuthProvider implements AuthenticationProvider{

	@Autowired
	private UserRepository userRespository;

	@Autowired
	private LoginRepository loginRespository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		UserAuth basicUserAuth = (UserAuth) authentication;

		Login login = loginRespository.findLoginByEmail(basicUserAuth.getEmail());
		
		if(login == null) {
			return new UserAuth(false, null, null, null, null);
		}

		User user = userRespository.findUserById(login.getId());
		
		if(passwordEncoder.matches(basicUserAuth.getPassword(), login.getPassword())) {
			
			Set<GrantedAuthority> authoritySet = new HashSet<GrantedAuthority>(); 
			for(Authority auth : user.getAuthorities() ) {
				authoritySet.add(new SimpleGrantedAuthority(auth.getName()));
			}
			List<GrantedAuthority> authorityList = new ArrayList<>(authoritySet); 
					
			return new UserAuth(true, login.getEmail(), null, null, authorityList);
		}

		return new UserAuth(false, login.getEmail(), null, null, null);
	}

	
	@Override
	public boolean supports(Class<?> authentication) {
		return UserAuth.class.equals(authentication);
	}
}
