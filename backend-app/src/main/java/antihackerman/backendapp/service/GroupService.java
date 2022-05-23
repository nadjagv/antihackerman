package antihackerman.backendapp.service;

import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.exception.NotUniqueException;
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
        try{
            Group group = groupRep.getById(groupId);
            if(group==null){
                throw new NotFoundException("Group with id "+groupId+" does not exist.");
            }
            return group;
        }catch (Exception e){
            e.printStackTrace();
            throw new NotFoundException("Group with id "+groupId+" does not exist.");
        }
    }

    public List<User> getOwnersForGroup (Integer groupId) throws NotFoundException {
        try{
            Group group = groupRep.getById(groupId);
            if(group==null){
                throw new NotFoundException("Group with id "+groupId+" does not exist.");
            }
            return group.getOwners();
        }catch (Exception e){
            e.printStackTrace();
            throw new NotFoundException("Group with id "+groupId+" does not exist.");
        }
    }

    public List<RealEstate> getRealEstatesForGroup (Integer groupId) throws NotFoundException {
        try{
            Group group = groupRep.getById(groupId);
            if(group==null){
                throw new NotFoundException("Group with id "+groupId+" does not exist.");
            }
            return group.getRealEstates();
        }catch (Exception e){
            e.printStackTrace();
            throw new NotFoundException("Group with id "+groupId+" does not exist.");
        }

    }

    public List<User> getAllUsersForGroup (Integer groupId) throws NotFoundException {
        List<User> result = new ArrayList<>();
        try{
            Group group = groupRep.getById(groupId);
            if(group==null){
                throw new NotFoundException("Group with id "+groupId+" does not exist.");
            }

            result.addAll(group.getOwners());

            group.getRealEstates().stream().forEach(realEstate -> {
                result.addAll(realEstate.getTenants());
            });

            return result.stream().distinct().collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            throw new NotFoundException("Group with id "+groupId+" does not exist.");
        }


    }

    public List<User> getTenantsForGroup (Integer groupId) throws NotFoundException {
        List<User> result = new ArrayList<>();
        try{
            Group group = groupRep.getById(groupId);
            if(group==null){
                throw new NotFoundException("Group with id "+groupId+" does not exist.");
            }

            group.getRealEstates().stream().forEach(realEstate -> {
                result.addAll(realEstate.getTenants());
            });

            return result.stream().distinct().collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            throw new NotFoundException("Group with id "+groupId+" does not exist.");
        }


    }

    public Group createGroup(String name) throws NotUniqueException {
        Group group = groupRep.findOneByName(name);
        if(group!=null){
            throw new NotUniqueException("Group with name "+name+" exists.");
        }

        Group newGroup = Group.builder()
                .deleted(false)
                .name(name)
                .build();
        return groupRep.save(newGroup);

    }

}
