package antihackerman.backendapp;

import antihackerman.backendapp.service.CertificateService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.X509Certificate;

@SpringBootApplication
public class BackendAppApplication {

	public static void main(String[] args){
		Security.addProvider(new BouncyCastleProvider());
		CertificateService cs = new CertificateService();
		X509Certificate cert = cs.createNewSelfSignedCertificate();
		System.out.println(cert);
		
		

		SpringApplication.run(BackendAppApplication.class, args);
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:3000").allowedMethods("*");
			}
		};
	}

}
