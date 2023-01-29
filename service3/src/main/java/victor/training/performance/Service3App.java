package victor.training.performance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@EnableBinding(Sink.class)
@RestController
@SpringBootApplication
public class Service3App {
  public static void main(String[] args) {
    SpringApplication.run(Service3App.class, args);
  }

  @Autowired
  private RestTemplate rest;

  @GetMapping("/service3")
  public String service3() {
    log.info("Start service 3..");
    List<String> responses4 = new ArrayList<>();
    List<Long> childrenIds = Arrays.asList(1L, 2L, 3L);
    for (Long childId : childrenIds) { // famous N+1 queries problem, with REST
      log.info("{}: Calling service 4", childId);
      responses4.add(rest.getForObject("http://localhost:8084/service4/{id}", String.class, childId));
      log.info("{}: Calling service 4", childId);
    }
    log.info("End service 3");
    return "from3 " + String.join(" ", responses4);
  }

  @ServiceActivator(inputChannel = "input")
  public void acceptNewReservation(String message) throws InterruptedException {
    System.out.println("Message received.");
    log.info("Received message " + message);
    Thread.sleep(50); // processing message takes time
    log.debug("Done processing message");
  }
}

