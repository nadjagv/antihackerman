package antihackerman.backendapp.repository;

import antihackerman.backendapp.model.DeviceAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceAlarmRepository extends JpaRepository<DeviceAlarm, Integer> {
}
