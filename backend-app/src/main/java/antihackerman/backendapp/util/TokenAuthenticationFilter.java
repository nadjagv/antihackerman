package antihackerman.backendapp.util;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import antihackerman.backendapp.exceptions.BlacklistedTokenException;
import antihackerman.backendapp.model.BlacklistedJWT;
import antihackerman.backendapp.repository.BlacklistRepository;
import antihackerman.backendapp.service.BlacklistService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
	
	private TokenUtils tokenUtils;

	private UserDetailsService userDetailsService;
	
	@Autowired
	private BlacklistRepository blacklistRepository;
	
	protected final Log LOGGER = LogFactory.getLog(getClass());

	public TokenAuthenticationFilter(TokenUtils tokenHelper, UserDetailsService userDetailsService) {
		this.tokenUtils = tokenHelper;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		if (blacklistRepository == null) { //Lazy Load because filter
		    ServletContext servletContext = request.getServletContext();
		    WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		    blacklistRepository = webApplicationContext.getBean(BlacklistRepository.class);
		}


		String username;
		
		String authToken = tokenUtils.getToken(request);
		String fingerprint = tokenUtils.getFingerprintFromCookie(request);

		try {
	
			if (authToken != null ) {
				username = tokenUtils.getUsernameFromToken(authToken);
				if (username != null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					if(blacklistRepository.existsByJwt(authToken)) {
						throw new BlacklistedTokenException("Token is blacklisted");
					}
					if (tokenUtils.validateToken(authToken, userDetails, fingerprint)) {
						TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
						authentication.setToken(authToken);
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
			}
			
		} catch (ExpiredJwtException ex) {
			LOGGER.debug("Token expired!");
		} catch (BlacklistedTokenException e) {
			LOGGER.debug("Token is blacklisted");
		} 
		
		chain.doFilter(request, response);
	}

}
