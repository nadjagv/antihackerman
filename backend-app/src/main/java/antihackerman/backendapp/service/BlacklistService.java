package antihackerman.backendapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antihackerman.backendapp.model.BlacklistedJWT;
import antihackerman.backendapp.repository.BlacklistRepository;

@Service
public class BlacklistService {
	
	@Autowired
	private BlacklistRepository blacklistRepository;
	
	public void save(String jwt) {
		blacklistRepository.save(new BlacklistedJWT(jwt));
	}
	
	public BlacklistedJWT findByJwt(String jwt) {
		return blacklistRepository.findByJwt(jwt);
	}

}
