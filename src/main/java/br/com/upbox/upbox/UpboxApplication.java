package br.com.upbox.upbox;

import br.com.upbox.config.SwaggerConfig;
import br.com.upbox.controller.UsuarioController;
import br.com.upbox.repository.UsuarioRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {SwaggerConfig.class, UsuarioController.class, UsuarioRepository.class})
public class UpboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpboxApplication.class, args);
	}

}
