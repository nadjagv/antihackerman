package antihackerman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import antihackerman.dto.Message;
import antihackerman.dto.SimpleMessage;


@Service
public class NotificationService {
	
	private final SimpMessagingTemplate messagingTemplate;
	
	@Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
	
	public void userNotification(String msg,String username) {
    	Message message = new Message(msg,username);

        messagingTemplate.convertAndSend("/topic/user-notification", message);
    }
	
	public void simpleNotification(String msg) {
    	SimpleMessage message = new SimpleMessage(msg);

        messagingTemplate.convertAndSend("/topic/simple-notification", message);
    }

}