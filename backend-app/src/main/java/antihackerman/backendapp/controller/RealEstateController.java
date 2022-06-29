package antihackerman.backendapp.controller;

import antihackerman.backendapp.dto.RealEstateDTO;
import antihackerman.backendapp.exception.NotFoundException;
import antihackerman.backendapp.logs.LogType;
import antihackerman.backendapp.model.RealEstate;
import antihackerman.backendapp.service.LogService;
import antihackerman.backendapp.service.RealEstateService;
import antihackerman.backendapp.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/" })
@RestController
@RequestMapping("/real-estates")
public class RealEstateController {
    @Autowired
    private RealEstateService realEstateService;

    @Autowired
    private LogService logService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_REALESTATE')")
    public ResponseEntity<RealEstateDTO> getOne(@PathVariable Integer id, @RequestHeader (name="Authorization") String token, HttpServletRequest request){

        try {
            RealEstate realEstate = realEstateService.getById(id);
            String username=tokenUtils.getUsernameFromToken(token.substring(7));

            logService.createLog(LogType.INFO, username, request.getRemoteAddr(), "User: "+username+" collected real estate by id.");

            return new ResponseEntity<RealEstateDTO>(new RealEstateDTO(realEstate), HttpStatus.OK);
        } catch (NotFoundException e) {
            e.printStackTrace();
            String username=tokenUtils.getUsernameFromToken(token.substring(7));

            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested real estate not in database");

            return new ResponseEntity<RealEstateDTO>(new RealEstateDTO(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping()
    @PreAuthorize("hasAuthority('CREATE_REALESTATE')")
    public ResponseEntity<RealEstateDTO> createRealEstate(@RequestBody RealEstateDTO dto, @RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        if (dto.getGroupId() == null || dto.getLocation() == null || dto.getName() == null || dto.getLocation().equals("") || dto.getName().equals("")){
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent create real estate request that is missing data.");

            return new ResponseEntity<RealEstateDTO>(new RealEstateDTO(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            RealEstate realEstate = realEstateService.createRealestate(dto);
            logService.createLog(LogType.SUCCESS, username, request.getRemoteAddr(), "User: "+username+" created real estate.");

            return new ResponseEntity<RealEstateDTO>(new RealEstateDTO(realEstate), HttpStatus.OK);
        }  catch (NotFoundException e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested creating real estate in group not in database.");

            return new ResponseEntity<RealEstateDTO>(new RealEstateDTO(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested creating real estate, but exception occured.");

            return new ResponseEntity<RealEstateDTO>(new RealEstateDTO(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/add-tenant/{userId}")
    @PreAuthorize("hasAuthority('EDIT_REALESTATE')")
    public ResponseEntity<String> addTenant(@PathVariable Integer userId, @RequestBody List<Integer> realEstateIds, @RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));
        if (realEstateIds == null){
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent edit real estate request that is missing data.");

            return new ResponseEntity<String>("Exception", HttpStatus.UNPROCESSABLE_ENTITY);
        }else if (realEstateIds.isEmpty()){
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent edit real estate request that is missing data.");

            return new ResponseEntity<String>("Exception", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            realEstateService.editRealEstates(userId, realEstateIds, true);
            logService.createLog(LogType.SUCCESS, username, request.getRemoteAddr(), "User: "+username+" edited real estate.");

            return new ResponseEntity<String>("Success", HttpStatus.OK);
        }  catch (NotFoundException e) {
            e.printStackTrace();
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent edit real estate request but real estate not in database.");
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" sent edit real estate request but real estate not in database.");

            return new ResponseEntity<String>("Exception", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" sent edit real estate request-exception occured.");

            return new ResponseEntity<String>("Exception", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/remove-tenant/{userId}")
    @PreAuthorize("hasAuthority('EDIT_REALESTATE')")
    public ResponseEntity<String> removeTenant(@PathVariable Integer userId, @RequestBody List<Integer> realEstateIds, @RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));

        if (realEstateIds == null){
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent edit real estate request that is missing data.");

            return new ResponseEntity<String>("Exception", HttpStatus.UNPROCESSABLE_ENTITY);
        }else if (realEstateIds.isEmpty()){
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent edit real estate request that is missing data.");

            return new ResponseEntity<String>("Exception", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            realEstateService.editRealEstates(userId, realEstateIds, false);
            logService.createLog(LogType.SUCCESS, username, request.getRemoteAddr(), "User: "+username+" edited real estate.");

            return new ResponseEntity<String>("Success", HttpStatus.OK);
        }  catch (NotFoundException e) {
            e.printStackTrace();
            logService.createLog(LogType.WARNING, username, request.getRemoteAddr(), "User: "+username+" sent edit real estate request but real estate not in database.");
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" sent edit real estate request but real estate not in database.");

            return new ResponseEntity<String>("Exception", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" sent edit real estate request-exception occured.");

            return new ResponseEntity<String>("Exception", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_REALESTATE')")
    public ResponseEntity<String> deleteById(@PathVariable Integer id, @RequestHeader (name="Authorization") String token, HttpServletRequest request){
        String username=tokenUtils.getUsernameFromToken(token.substring(7));

        try {
            realEstateService.deleteById(id);
            logService.createLog(LogType.SUCCESS, username, request.getRemoteAddr(), "User: "+username+" deleted real estate.");

            return new ResponseEntity<String>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logService.createLog(LogType.ERROR, username, request.getRemoteAddr(), "User: "+username+" requested delete real estate but real estate not in database.");

            return new ResponseEntity<String>("Real estate not in database.", HttpStatus.NOT_FOUND);
        }
    }
}
