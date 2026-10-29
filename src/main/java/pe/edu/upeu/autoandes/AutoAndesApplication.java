package pe.edu.upeu.autoandes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AutoAndesApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutoAndesApplication.class, args);
    }
}
