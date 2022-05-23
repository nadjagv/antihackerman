package antihackerman.backendapp.service;

import antihackerman.backendapp.dto.RealEstateDTO;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.model.Group;
import antihackerman.backendapp.model.RealEstate;
import antihackerman.backendapp.model.User;
import antihackerman.backendapp.repository.GroupRepository;
import antihackerman.backendapp.repository.RealEstateRepository;
import antihackerman.backendapp.repository.RoleRepository;
import antihackerman.backendapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RealEstateService {
    @Autowired
    private RealEstateRepository realEstateRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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

    public void editRealEstates(Integer tenantId, List<Integer> realEstateIds, boolean adding) throws NotFoundException {
        User tenant = userRepository.getById(tenantId);
        if (tenant == null){
            throw new NotFoundException("User with id " + tenantId + " not found.");
        }

        if (adding){
            for (Integer id: realEstateIds) {
                addTenant(id, tenantId);
            }
            return;
        }

        for (Integer id: realEstateIds) {
            removeTenant(id, tenantId);
        }

    }

    public RealEstate addTenant(Integer realEstateId, Integer tenantId) throws NotFoundException {
        User tenant = userRepository.getById(tenantId);
        if (tenant == null){
            throw new NotFoundException("User with id " + tenantId + " not found.");
        }

        RealEstate realEstate = realEstateRepository.getById(realEstateId);
        if (realEstate == null){
            throw new NotFoundException("User with id " + realEstateId + " not found.");
        }

        tenant.getRoles().add(roleRepository.findOneByRole("ROLE_TENANT"));
        tenant.setRoles(tenant.getRoles().stream().distinct().collect(Collectors.toList()));
        tenant.getRealestatesTenanting().add(realEstate);
        userRepository.save(tenant);

        realEstate.getTenants().add(tenant);
        return realEstateRepository.save(realEstate);
    }

    public RealEstate removeTenant(Integer realEstateId, Integer tenantId) throws NotFoundException {
        User tenant = userRepository.getById(tenantId);
        if (tenant == null){
            throw new NotFoundException("User with id " + tenantId + " not found.");
        }

        RealEstate realEstate = realEstateRepository.getById(realEstateId);
        if (realEstate == null){
            throw new NotFoundException("User with id " + realEstateId + " not found.");
        }
        tenant.getRealestatesTenanting().remove(realEstate);
        if (tenant.getRealestatesTenanting().isEmpty() && tenant.getGroupsOwning().isEmpty()){
            tenant.getRoles().remove(roleRepository.findOneByRole("ROLE_TENANT"));
            tenant.getRoles().remove(roleRepository.findOneByRole("ROLE_OWNER"));
        }

        userRepository.save(tenant);

        realEstate.getTenants().remove(tenant);
        return realEstateRepository.save(realEstate);
    }


    public void deleteById(Integer realEstateId) throws NotFoundException {
        RealEstate realEstate = realEstateRepository.getById(realEstateId);
        if (realEstate == null){
            throw new NotFoundException("RealEstate with id " + realEstateId + " not found.");
        }

        for (User tenant: realEstate.getTenants()) {
            tenant.getRealestatesTenanting().remove(realEstate);
            userRepository.save(tenant);
        }

        realEstateRepository.deleteById(realEstateId);
    }
}
