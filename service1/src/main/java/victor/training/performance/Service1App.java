package victor.training.performance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@Slf4j
@RestController
@SpringBootApplication
public class Service1App {
  public static void main(String[] args) {
    SpringApplication.run(Service1App.class, args);
  }

  @Autowired
  private RestTemplate rest;

  @GetMapping
  public String get() {
    log.info("Start service 1");
    String response = rest.getForObject("http://localhost:8082/service2", String.class);
    log.info("End service 1");
    return "Hi... from1 " + response;
  }
}
