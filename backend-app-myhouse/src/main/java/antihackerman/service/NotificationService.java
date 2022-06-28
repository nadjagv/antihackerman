package antihackerman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import antihackerman.dto.Message;


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

}