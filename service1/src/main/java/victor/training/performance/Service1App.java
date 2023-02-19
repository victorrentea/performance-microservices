package victor.training.performance;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableAsync
@SpringBootApplication
public class Service1App {
  public static void main(String[] args) {
    SpringApplication.run(Service1App.class, args);
  }
}

@Slf4j
@RestController
class Service1Controller {
  @Autowired
  private RestTemplate rest;
  @Autowired
  private AsyncService asyncService;

  @GetMapping("{id}")
  public Response get(@PathVariable Long id) {
    log.info("Start");
    String three = rest.getForObject("http://localhost:8083/"+id, String.class);
    String four = rest.getForObject("http://localhost:8084/" +id, String.class);
    asyncService.fireAndForget();
    return new Response(three, four);
  }

  public static class Response {
    public String three;
    public String four;

    public Response(String three, String four) {
      this.three = three;
      this.four = four;
    }
  }
}


@Slf4j
@Service
class AsyncService {

  @SneakyThrows
  @Async
  public void fireAndForget() {
    log.info("Start");
    Thread.sleep(200);
    log.info("End");
  }
}