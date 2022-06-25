package antihackerman.service;

import antihackerman.exceptions.NotFoundException;
import antihackerman.model.Group;
import antihackerman.model.User;
import antihackerman.repository.GroupRepository;
import antihackerman.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRep;

    @Autowired
    private UserRepository userRepository;

    public Set<Group> getAllForUser(Integer id) throws NotFoundException {
        User user = userRepository.getById(id);
        if (user == null){
            throw new NotFoundException("User with id " + id + " not found.");
        }

        Set<Group> result = new HashSet<>();

        result.addAll(user.getGroupsOwning());
        result.addAll(user.getGroupsTenanting());

        return result.stream().distinct().collect(Collectors.toSet());
    }
    
    public Set<Group> getAllForUserByUsername(String username) throws NotFoundException {
        User user = userRepository.findOneByUsername(username);
        if (user == null){
            throw new NotFoundException("User with username " + username + " not found.");
        }

        Set<Group> result = new HashSet<>();

        result.addAll(user.getGroupsOwning());
        result.addAll(user.getGroupsTenanting());

        return result.stream().distinct().collect(Collectors.toSet());
    }

    public Set<Group> getOwnedForUser(Integer id) throws NotFoundException {
        User user = userRepository.getById(id);
        if (user == null){
            throw new NotFoundException("User with id " + id + " not found.");
        }

        return user.getGroupsOwning();
    }

    public Set<Group> getTenantingForUser(Integer id) throws NotFoundException {
        User user = userRepository.getById(id);
        if (user == null){
            throw new NotFoundException("User with id " + id + " not found.");
        }

        return user.getGroupsTenanting();
    }
}
