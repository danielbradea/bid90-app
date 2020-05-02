package com.bid90.app.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bid90.app.security.JwtProvider;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		Authentication authentication = null;
		String authToken = null;
		try {
			authToken = request.getHeader(jwtProvider.getTokenHeader()).replace(jwtProvider.getTokenPrefix()+" ", "");
			authentication = jwtProvider.getAuthentication(authToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			// TODO: handle exception
		}

		chain.doFilter(request, response);
	}
}
