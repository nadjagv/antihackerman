package antihackerman.logs;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogsRepository extends MongoRepository<Log, String>{

}
