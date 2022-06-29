package antihackerman.backendapp.repository;

import antihackerman.backendapp.model.LogAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogAlarmRepository extends JpaRepository<LogAlarm, Integer> {
}
