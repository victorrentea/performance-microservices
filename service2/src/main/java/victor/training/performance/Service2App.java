package victor.training.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import victor.training.performance.support.Two;
import victor.training.performance.support.TwoCategory;
import victor.training.performance.support.TwoRepo;
import victor.training.performance.support.TwoStatus;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@SpringBootApplication
@Transactional
public class Service2App {
  private static final Logger log = LoggerFactory.getLogger(Service2App.class);

  public static void main(String[] args) {
    SpringApplication.run(Service2App.class, args);
  }
  @Autowired
  private RestTemplate rest;
  @Autowired
  private TwoRepo repo;



  @EventListener(ApplicationStartedEvent.class)
  public void insertDummyData() {
    repo.saveAll(IntStream.range(0, 20).mapToObj(this::dummyData).collect(Collectors.toList()));
  }

  private Two dummyData(int i) {
    return new Two("John Doe " + UUID.randomUUID())
            .setStatus(new TwoStatus("Status" + i))
            .setThreeSourceId(1L)
            .setThreeDestinationId(2L)
            .setCategory(new TwoCategory("Category" + i));
  }

  @GetMapping("/service2")
  public String service2() throws InterruptedException {
    log.info("Start service 2...");

    log.info("Now let's simulate working with Files, DB query, CPU task...");
    Thread.sleep(80);
    log.info("Long job done...");

    log.info("Calling service3");
    String response = rest.getForObject("http://localhost:8083/service3", String.class);
    log.info("Got response from service3");
    return "from2 " + response;
  }
}
