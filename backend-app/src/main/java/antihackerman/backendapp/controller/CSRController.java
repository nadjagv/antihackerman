package antihackerman.backendapp.controller;


import antihackerman.backendapp.dto.CSRdto;
import antihackerman.backendapp.model.SubjectData;
import antihackerman.backendapp.service.CSRService;
import antihackerman.backendapp.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;

@RestController
@RequestMapping("/csr")
public class CSRController {

    @Autowired
    CSRService csrService;

    @PostMapping(path = "/uploadCSR", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadCSR(@RequestParam(("file")) MultipartFile q) throws Exception {
        String csr = FileUtil.readFile(q);
        csrService.addRequest(csr);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/generateCSR")
    public ResponseEntity generateCSR(@RequestBody CSRdto dto) throws Exception {
        SubjectData subjectData = csrService.generateSubjectDataCSR(dto);
        csrService.generateCSR(subjectData, new ArrayList<>());
        return ResponseEntity.ok().build();
    }
}