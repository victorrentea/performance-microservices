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
public class Service2App {
  public static void main(String[] args) {
    SpringApplication.run(Service2App.class, args);
  }

  @Autowired
  private RestTemplate rest;

  @GetMapping("/service2")
  public String service2() throws InterruptedException {
    log.info("Start service 2...");

    log.info("Now let's create some intentional delay to emulate working with Files, DB query, CPU task...");
    Thread.sleep(2 * 1000);
    log.info("Long job done...");

    log.info("Calling service3");
    String response = rest.getForObject("http://localhost:8083/service3", String.class);
    log.info("Got response from service3");
    return "from2 " + response;
  }
}