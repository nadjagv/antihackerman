package antihackerman.backendapp.service;

import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.model.Group;
import antihackerman.backendapp.model.RealEstate;
import antihackerman.backendapp.model.User;
import antihackerman.backendapp.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    GroupRepository groupRep;

    public List<Group> getAll (){
        return  groupRep.findAll();
    }

    public Group getGroupById (Integer groupId) throws NotFoundException {
        Group result = groupRep.getById(groupId);
        if(result==null){
            throw new NotFoundException("Group with id "+groupId+" does not exist.");
        }
        return result;
    }

    public List<User> getOwnersForGroup (Integer groupId) throws NotFoundException {
        Group group = groupRep.getById(groupId);
        if(group==null){
            throw new NotFoundException("Group with id "+groupId+" does not exist.");
        }
        return group.getOwners();
    }

    public List<RealEstate> getRealEstatesForGroup (Integer groupId) throws NotFoundException {
        Group group = groupRep.getById(groupId);
        if(group==null){
            throw new NotFoundException("Group with id "+groupId+" does not exist.");
        }
        return group.getRealEstates();
    }

    public List<User> getAllUsersForGroup (Integer groupId) throws NotFoundException {
        Group group = groupRep.getById(groupId);
        if(group==null){
            throw new NotFoundException("Group with id "+groupId+" does not exist.");
        }

        List<User> result = new ArrayList<>();
        result.addAll(group.getOwners());
//        for (RealEstate realEstate: group.getRealEstates()) {
//            result.addAll(realEstate.getTenants());
//        }

        group.getRealEstates().stream().forEach(realEstate -> {
            result.addAll(realEstate.getTenants());
        });

        return result.stream().distinct().collect(Collectors.toList());
    }

    public List<User> getTenantsForGroup (Integer groupId) throws NotFoundException {
        Group group = groupRep.getById(groupId);
        if(group==null){
            throw new NotFoundException("Group with id "+groupId+" does not exist.");
        }

        List<User> result = new ArrayList<>();
        group.getRealEstates().stream().forEach(realEstate -> {
            result.addAll(realEstate.getTenants());
        });

        return result.stream().distinct().collect(Collectors.toList());
    }
}
