package junsang.cho.catchpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CatchProApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatchProApplication.class, args);
    }

}
