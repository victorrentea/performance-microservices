package victor.training.performance;

import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

  private final Map<String, Deque<String>> lastNamesSearchedByStatus = Collections.synchronizedMap(new HashMap<>());

  @GetMapping("/search")
  public  List<String> search(
          @RequestParam(required = false) String name,
          @RequestParam(required = false) String status) {
    return doSearch(name, status);
  }

  private synchronized List<String> doSearch(String name, String status) {
    if (status != null) {
      Deque<String> deque = lastNamesSearchedByStatus.computeIfAbsent(status, k -> new LinkedList<>());
      deque.add(name);
      if (deque.size() > 10) deque.removeLast();
    }
    return repo.search(name, status).stream().map(Two::getName).collect(Collectors.toList());
  }

  @PostMapping("{name}")
  public String create(@PathVariable String name) {
    Two two = new Two(name);
    String four = rest.getForObject("http://localhost:8084/" + two.getName(), String.class);
    repo.save(two.setFour(four));
    rest.postForObject("http://localhost:8083/send-email/"+two.getId(), null, String.class);
    return "Ok";
  }

  @GetMapping("{id}")
  @Timed
  public TwoView getById(@PathVariable Long id) throws InterruptedException {
    Two two = repo.findById(id).orElseThrow();
//    String sourceName = rest.getForObject("http://localhost:8083/" + two.getThreeSourceId(), String.class);
//    String destinationName = rest.getForObject("http://localhost:8083/" + two.getThreeDestinationId(), String.class);

    Thread.sleep(100);
    List<String> twoForOneCall = rest.getForObject("http://localhost:8083/many?ids="
        + two.getThreeDestinationId()+","+two.getThreeSourceId(), List.class);
    String destinationName = twoForOneCall.get(0);
    String sourceName = twoForOneCall.get(1);

    return new TwoView(two.getId(), two.getName(), sourceName, destinationName);
  }



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


  @Bean
  public static RestTemplate customRestTemplate() {
    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    clientHttpRequestFactory.setConnectTimeout(5000);
    return new RestTemplate(clientHttpRequestFactory);
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
