package antihackerman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import antihackerman.model.Group;


@Repository
public interface GroupRepository extends JpaRepository<Group, Integer>{
	Group findOneByName(String name);

}
