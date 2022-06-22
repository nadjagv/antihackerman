package antihackerman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendAppMyhouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendAppMyhouseApplication.class, args);
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				.allowedOrigins("https://localhost:3010")
				.allowedMethods("*")
				.allowedHeaders("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization")
				.allowCredentials(true);
			}
		};
	}

}
