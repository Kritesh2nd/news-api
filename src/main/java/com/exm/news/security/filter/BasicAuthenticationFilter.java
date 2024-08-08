package com.exm.news.security.filter;

import java.util.Base64;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.exm.news.security.manager.AuthManager;

import com.exm.news.security.authentication.UserAuth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class BasicAuthenticationFilter extends OncePerRequestFilter{

	private final AuthManager authManager;

	private final HandlerExceptionResolver handlerExceptionResolver;

    public BasicAuthenticationFilter(AuthManager authManager, HandlerExceptionResolver handlerExceptionResolver) {
        this.authManager = authManager;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, BadCredentialsException {

		System.out.println("BasicAuthenticationFilter");
		final String authHeader = request.getHeader("Authorization");

		if(authHeader == null) {
			filterChain.doFilter(request, response);
			return;
		}

		if(authHeader.startsWith("Basic ")) {
			String username = extractUsernameAndPassword(authHeader)[0];
			String password = extractUsernameAndPassword(authHeader)[1];
			UserAuth ua = new UserAuth(false, username,password,null,null);
			
			UserAuth auth = (UserAuth) authManager.authenticate(ua);

			if(auth.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			else {
				handlerExceptionResolver.resolveException(request, response, null, new AccessDeniedException("Bad Credentials"));
				return;
			}
		
		}

		filterChain.doFilter(request, response);
	}
	
	
	private String[] extractUsernameAndPassword(String authorization) {
		String base64Credentials = authorization.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);

        return credentials.split(":", 2); // values = [username, password]

	}
}
