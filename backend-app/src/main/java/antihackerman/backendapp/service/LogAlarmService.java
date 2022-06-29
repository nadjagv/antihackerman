package antihackerman.backendapp.service;

import antihackerman.backendapp.dto.LogAlarmDTO;
import antihackerman.backendapp.exception.InvalidInputException;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.logs.Log;
import antihackerman.backendapp.model.LogAlarm;
import antihackerman.backendapp.repository.LogAlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LogAlarmService {

    @Autowired
    private LogAlarmRepository logAlarmRepository;

    public List<LogAlarm> getAll(){
        return logAlarmRepository.findAll();
    }

    public LogAlarm createLogAlarm(LogAlarmDTO dto) throws InvalidInputException {
        if (dto.getName() == null){
            throw new InvalidInputException("Invalid data in creating LogAlarm - name must not be null.");
        }
        LogAlarm logAlarm = LogAlarm.builder()
                .name(dto.getName())
                .logType(dto.getLogType())
                .username(dto.getUsername())
                .charSequence(dto.getCharSequence())
                .deleted(false)
                .build();

        return  logAlarmRepository.save(logAlarm);

    }

    public void deleteLogAlarm(Integer id) throws NotFoundException {
        LogAlarm logAlarm = logAlarmRepository.getById(id);
        if (logAlarm==null) {
            throw new NotFoundException("LogAlarm with id " + id + " not found.");
        }

        logAlarmRepository.delete(logAlarm);
    }
}
