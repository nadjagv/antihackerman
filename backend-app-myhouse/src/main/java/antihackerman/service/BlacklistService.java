package antihackerman.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antihackerman.model.BlacklistedJWT;
import antihackerman.repository.BlacklistRepository;


@Service
public class BlacklistService {
	
	@Autowired
	private BlacklistRepository blacklistRepository;
	
	public void save(String jwt) {
		blacklistRepository.save(new BlacklistedJWT(jwt));
	}
	
	public Optional<BlacklistedJWT> findByJwt(String jwt) {
		return blacklistRepository.findByJwt(jwt);
	}
	
	public boolean exists(String jwt) {
		return blacklistRepository.existsByJwt(jwt);
	}

}
