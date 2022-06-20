package antihackerman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import antihackerman.model.RealEstate;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Integer> {

}
