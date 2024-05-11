package com.sky.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sky.constants.JwtConstant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class JwtTokenValidator extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
			FilterChain filterChain) throws ServletException, IOException {
		
		// Extract JWT token from request header
		String jwt = request.getHeader(JwtConstant.JWT_HEADER);
		
		if (null != jwt) {
			// Removing "Bearer " prefix from token
			jwt = jwt.substring(7);
			try {
				// Decoding JWT token using secret key
				SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
				Claims claims = Jwts.parserBuilder()
						.setSigningKey(key)
						.build()
						.parseClaimsJws(jwt)
						.getBody();

				// Extracting email and authorities from JWT claims
				String email = String.valueOf(claims.get("email"));
				String authorities = String.valueOf(claims.get("authorities"));

				// Converting authorities to list of GrantedAuthority
				List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				// Creating authentication object
				Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
				// Setting authentication in Security Context
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				// Throw exception for invalid token
				throw new BadCredentialsException("Invalid Token...");
			}
		}
		
		// Continue filter chain
		filterChain.doFilter(request, response);
	}
}
