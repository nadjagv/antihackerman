package antihackerman.backendapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import antihackerman.backendapp.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findOneByUsername(String username);

	User findOneByEmail(String email);

}
