package antihackerman.backendapp.repository;

import antihackerman.backendapp.model.IntervalDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervalDeviceRepository extends JpaRepository<IntervalDevice, Integer> {
}
