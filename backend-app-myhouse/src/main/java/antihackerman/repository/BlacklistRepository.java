package antihackerman.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import antihackerman.model.BlacklistedJWT;

@Repository
public interface BlacklistRepository extends JpaRepository<BlacklistedJWT, Integer> {
	
	public Optional<BlacklistedJWT> findByJwt(String jwt);
	
	public boolean existsByJwt(String jwt);

}
