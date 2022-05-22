package antihackerman.backendapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import antihackerman.backendapp.model.BlacklistedJWT;

public interface BlacklistRepository extends JpaRepository<BlacklistedJWT, Integer> {

	public Optional<BlacklistedJWT> findByJwt(String jwt);
	
	public boolean existsByJwt(String jwt);
}
