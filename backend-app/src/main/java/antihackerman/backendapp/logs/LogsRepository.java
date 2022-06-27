package antihackerman.backendapp.logs;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LogsRepository extends MongoRepository<Log, String>{

	@Query("{ $or : [ { $where: '?0 == null' } , { 'type' : ?0 } ] },"
			+ "{ $or : [ { $where: '?1 == null' } , { 'username' : ?1 } ] }")
	public List<Log> findLogs(LogType type,String user);
	
}
