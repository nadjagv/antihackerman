package antihackerman.backendapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import antihackerman.backendapp.model.BlacklistedJWT;

public interface BlacklistRepository extends JpaRepository<BlacklistedJWT, Integer> {

	public BlacklistedJWT findByJwt(String jwt);
}
