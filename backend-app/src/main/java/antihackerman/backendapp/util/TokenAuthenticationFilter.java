package antihackerman.backendapp.util;

import java.io.IOException;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
	
	private TokenUtils tokenUtils;

	private UserDetailsService userDetailsService;
	
	protected final Log LOGGER = LogFactory.getLog(getClass());

	public TokenAuthenticationFilter(TokenUtils tokenHelper, UserDetailsService userDetailsService) {
		this.tokenUtils = tokenHelper;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String username;
		String authToken = tokenUtils.getToken(request);
		try {
	
			if (authToken != null) {
				String[] chunks = authToken.split("\\.");
				String tokenWithoutSignature = chunks[0] + "." + chunks[1];
				String signature = chunks[2];
				
				SignatureAlgorithm sa = tokenUtils.SIGNATURE_ALGORITHM;
				SecretKeySpec secretKeySpec = new SecretKeySpec(tokenUtils.SECRET.getBytes(), sa.getJcaName());
				DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa,secretKeySpec);
				
				if (!validator.isValid(tokenWithoutSignature, signature)) {
				    throw new Exception("Could not verify JWT token integrity!");
				}
				
				username = tokenUtils.getUsernameFromToken(authToken);
				if (username != null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					if (tokenUtils.validateToken(authToken, userDetails)) {
						TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
						authentication.setToken(authToken);
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
			}
			
		} catch (ExpiredJwtException ex) {
			LOGGER.debug("Token expired!");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		chain.doFilter(request, response);
	}

}
