package antihackerman.backendapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
		
		System.out.println("========================================================");

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = (User) authentication.getPrincipal();
		
		if(user.getLastPasswordResetDate()!=null) {
			return new ResponseEntity<>("User disabled",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		String jwt = tokenUtils.generateToken(user);
		Long expiresIn = (long) tokenUtils.getExpiredIn();
		
		List<String> roles=new ArrayList<String>();
		for(Role r: user.getRoles()) {
			roles.add(r.getRole());
		}

		return new ResponseEntity<>(new UserTokenState(jwt, user.getUsername(), roles, expiresIn),HttpStatus.OK);
	}

}
