package antihackerman.backendapp.repository;

import antihackerman.backendapp.model.BooleanDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooleanDeviceRepository extends JpaRepository<BooleanDevice, Integer> {
}
