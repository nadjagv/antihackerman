package antihackerman.backendapp.service;

import antihackerman.backendapp.exception.UserNotFoundException;
import antihackerman.backendapp.model.User;
import antihackerman.backendapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRep;

    public List<User> getAll (){
        return userRep.findAll();
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        User result = userRep.findOneByUsername(username);
        if(result==null){
            throw new UserNotFoundException("User with username "+username+" does not exist.");
        }
        return result;
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        User result = userRep.findOneByEmail(email);
        if(result==null){
            throw new UserNotFoundException("User with email "+email+" does not exist.");
        }
        return result;
    }

    public User deleteByUsername(String username) throws UserNotFoundException {
        User result = userRep.findOneByUsername(username);
        if(result==null){
            throw new UserNotFoundException("User with username "+username+" does not exist.");
        }
        result.setObrisan(true);
        return userRep.save(result);
    }
}
