package antihackerman.backendapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.AccountLockedException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import antihackerman.backendapp.dto.JwtAuthenticationRequest;
import antihackerman.backendapp.dto.UserTokenState;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.model.Role;
import antihackerman.backendapp.model.User;
import antihackerman.backendapp.service.BlacklistService;
import antihackerman.backendapp.service.UserService;
import antihackerman.backendapp.util.TokenUtils;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private BlacklistService blacklistService;
	
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<Object> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
		
		try{
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			User user = (User) authentication.getPrincipal();
			
			user.setWrongLogins(0);
			userService.saveUser(user);
			
			String fingerprint = tokenUtils.generateFingerprint();
	        String jwt = tokenUtils.generateToken(user, fingerprint);
	        Long expiresIn = (long) tokenUtils.getExpiredIn();
	        
	        String cookie = "Fingerprint=" + fingerprint + "; HttpOnly; Path=/";
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Set-Cookie", cookie);
	        
	        ArrayList<String> roles=new ArrayList<String>();
	        for(Role r: user.getRoles()) {
	        	roles.add(r.getRole());
	        }

			return ResponseEntity.ok().headers(headers).body(new UserTokenState(jwt, user.getUsername(), expiresIn, roles));
		}catch(BadCredentialsException e) {
			try {
				User user=userService.getUserByUsername(authenticationRequest.getUsername());
				user.setWrongLogins(user.getWrongLogins()+1);
				userService.saveUser(user);
			} catch (NotFoundException e1) {
			}
			return new ResponseEntity<Object>(null,HttpStatus.NOT_FOUND);
		}

	
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Object> logout(@RequestHeader (name="Authorization") String token){
		System.out.println(token.substring(7));
		this.blacklistService.save(token.substring(7));
		return new ResponseEntity<Object>(null,HttpStatus.OK);
	}

}
