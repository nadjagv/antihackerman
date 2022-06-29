package antihackerman.repository;

import antihackerman.model.LogAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogAlarmRepository extends JpaRepository<LogAlarm, Integer> {
}
