package junsang.cho.catchpro.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthCheckController {

    @GetMapping("/")
    public String healthCheck() {
        log.info("catchPro HEALTH....OK!!");
        return "catchPro HEALTH....OK!!";
    }
}
