package com.bid90.app.security;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bid90.app.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

	@Value("${JWT_SECURITY_KEY}")
	private String secret;

	@Value("${JWT_TOKEN_EXP}")
	private Long expiration;

	@Value("${JWT_TOKEN_HEADER}")
	private String tokenHeader;
	
	@Value("${JWT_TOKEN_PREFIX}")
	private String tokenPrefix;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserService userService;

	@Autowired
	AuthenticationManager authenticationManager;

	public String getTokenHeader() {
		return tokenHeader;
	}

	public String getTokenPrefix() {
		return tokenPrefix;
	}
	
	public String loginAndGetToken(String email, String password) {

		UsernamePasswordAuthenticationToken update = new UsernamePasswordAuthenticationToken(email, password);
		authenticationManager.authenticate(update);
		return this.create(email, password);
	}

	public String create(String email, Object claims) {
		final LocalDateTime localDataTimeNow = LocalDateTime.now();
		final ZonedDateTime createdDate = localDataTimeNow.atZone(ZoneId.systemDefault());
		final ZonedDateTime expirationDate = localDataTimeNow.plusMinutes(expiration).atZone(ZoneId.systemDefault());
		Map<String, Object> claim = new HashMap<String, Object>();

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		claim.put("private", claims);

		JwtBuilder builder = Jwts.builder().setClaims(claim).setSubject(email)
				.setIssuedAt(Date.from(createdDate.toInstant())).setExpiration(Date.from(expirationDate.toInstant()))
				.signWith(signatureAlgorithm, signingKey);

		return builder.compact();
	}

	public Claims decodeJWT(String jwt) {

		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).parseClaimsJws(jwt)
				.getBody();
		return claims;
	}

	public String getRoles(String token) {

		Claims body = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();

		String role = modelMapper.map(body.get("private", HashMap.class), String.class);

		return role;
	}

	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody().getSubject().toString();
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userService.loadUserByUsername(this.getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
}
