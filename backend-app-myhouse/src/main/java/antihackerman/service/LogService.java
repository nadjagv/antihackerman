package antihackerman.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antihackerman.logs.Log;
import antihackerman.logs.LogType;
import antihackerman.logs.LogsRepository;


@Service
public class LogService {
	
	@Autowired
	private LogsRepository logsRepository;
	
	
	public void createLog(LogType type,String username,String ipAddress,String message) {
		Log log=new Log(type, username, ipAddress, message);
        logsRepository.insert(log);
        //fire alarms
	}

}
