package antihackerman.service;

import antihackerman.exceptions.NotFoundException;
import antihackerman.model.Group;
import antihackerman.model.RealEstate;
import antihackerman.model.User;
import antihackerman.repository.GroupRepository;
import antihackerman.repository.RealEstateRepository;
import antihackerman.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RealEstateService {
    @Autowired
    private RealEstateRepository realEstateRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    public Set<RealEstate> getAllForUserAndGroup(Integer groupId, Integer userId) throws NotFoundException {
        Group group = groupRepository.getById(groupId);
        if (group == null){
            throw new NotFoundException("Group with id " + groupId + " not found.");
        }

        User user = userRepository.getById(userId);
        if (user == null){
            throw new NotFoundException("User with id " + userId + " not found.");
        }

        Set<RealEstate> allInGroup = new HashSet<RealEstate>(group.getRealEstates());

        if (group.getOwners().contains(user)){
            return allInGroup;
        }

        allInGroup.retainAll(user.getRealestatesTenanting());
        return allInGroup;


    }
    
    public Set<RealEstate> getAllForUserAndGroup(Integer groupId, String username) throws NotFoundException {
        Group group = groupRepository.getById(groupId);
        if (group == null){
            throw new NotFoundException("Group with id " + groupId + " not found.");
        }

        User user = userRepository.findOneByUsername(username);
        if (user == null){
            throw new NotFoundException("User with username " + username + " not found.");
        }

        Set<RealEstate> allInGroup = new HashSet<RealEstate>(group.getRealEstates());

        if (group.getOwners().contains(user)){
            return allInGroup;
        }

        allInGroup.retainAll(user.getRealestatesTenanting());
        return allInGroup;


    }
}
