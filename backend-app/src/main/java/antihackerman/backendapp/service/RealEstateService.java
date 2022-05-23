package antihackerman.backendapp.service;

import antihackerman.backendapp.dto.RealEstateDTO;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.model.Group;
import antihackerman.backendapp.model.RealEstate;
import antihackerman.backendapp.model.User;
import antihackerman.backendapp.repository.GroupRepository;
import antihackerman.backendapp.repository.RealEstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class RealEstateService {
    @Autowired
    private RealEstateRepository realEstateRepository;

    @Autowired
    private GroupRepository groupRepository;

    public RealEstate createRealestate(RealEstateDTO dto) throws NotFoundException {
        try {
            Group group = groupRepository.getById(dto.getGroupId());
            if (group == null){
                throw new NotFoundException("Group with id " + dto.getGroupId() + " not found.");
            }

            RealEstate newRealEstate = RealEstate.builder()
                    .deleted(false)
                    .location(dto.getLocation())
                    .name(dto.getName())
                    .group(group)
                    .tenants(new HashSet<>())
                    .build();
            return realEstateRepository.save(newRealEstate);
        }catch(Exception e){
            e.printStackTrace();
            throw new NotFoundException("Group with id " + dto.getGroupId() + " not found.");
        }
    }
}
