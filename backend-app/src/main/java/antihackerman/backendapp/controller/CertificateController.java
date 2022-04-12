package antihackerman.backendapp.controller;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import antihackerman.backendapp.service.CertificateService;

@RestController
@RequestMapping("/cert")
public class CertificateController {
	
	@Autowired
	private CertificateService certService;
	
	@GetMapping("/{alias}")
	public ResponseEntity<String> getCertificate(@PathVariable String alias){

		Certificate cert=certService.getCertificateByAlias(alias);
		return new ResponseEntity<String>(cert.toString(),HttpStatus.OK);

	}
	
	@GetMapping("/validity/{alias}")
	public ResponseEntity<Boolean> checkValidity(@PathVariable String alias){

		boolean check=certService.checkValidityForCertificate(alias);
		return new ResponseEntity<Boolean>(check,HttpStatus.OK);

	}

}
