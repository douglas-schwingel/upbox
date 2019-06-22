package br.com.upbox.upbox;

import br.com.upbox.config.MongoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {MongoConfig.class})
public class UpboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpboxApplication.class, args);
	}

}
