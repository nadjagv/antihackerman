package antihackerman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import antihackerman.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findOneByRole(String role);

}
