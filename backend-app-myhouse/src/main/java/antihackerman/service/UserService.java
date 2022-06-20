package antihackerman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antihackerman.exceptions.NotFoundException;
import antihackerman.model.User;
import antihackerman.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
    private UserRepository userRep;
	
	public void saveUser(User user) {
    	userRep.save(user);
    }
	
	public User getUserByUsername(String username) throws NotFoundException {
        User result = userRep.findOneByUsername(username);
        if(result==null){
            throw new NotFoundException("User with username "+username+" does not exist.");
        }
        return result;
    }

}
