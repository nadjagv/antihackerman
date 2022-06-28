package antihackerman.backendapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import antihackerman.backendapp.dto.Message;


@Service
public class NotificationService {
	
	private final SimpMessagingTemplate messagingTemplate;
	
	@Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
	
	public void simpleNotification(String msg) {
    	Message message = new Message(msg);

        messagingTemplate.convertAndSend("/topic/simple-notification", message);
    }

}
