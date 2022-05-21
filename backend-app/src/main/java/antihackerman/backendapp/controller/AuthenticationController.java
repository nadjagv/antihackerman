package antihackerman.backendapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import antihackerman.backendapp.dto.JwtAuthenticationRequest;
import antihackerman.backendapp.dto.UserTokenState;
import antihackerman.backendapp.model.Role;
import antihackerman.backendapp.model.User;
import antihackerman.backendapp.util.TokenUtils;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/login")
	public ResponseEntity<Object> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = (User) authentication.getPrincipal();
		String fingerprint = tokenUtils.generateFingerprint();
        String jwt = tokenUtils.generateToken(user, fingerprint);
        Long expiresIn = (long) tokenUtils.getExpiredIn();
        
        String cookie = "Fingerprint=" + fingerprint + "; HttpOnly; Path=/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", cookie);

		return ResponseEntity.ok().headers(headers).body(new UserTokenState(jwt, expiresIn));
	}

}
