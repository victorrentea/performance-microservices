package victor.training.performance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  @GetMapping("{id}")
  public Response get(@PathVariable Long id) {
    log.info("Entry ");
    String three = rest.getForObject("http://localhost:8083/"+id, String.class);
    String four = rest.getForObject("http://localhost:8084/" +id, String.class);
    return new Response(three, four);
  }

//  @Autowired
//  private WebClient webClient;
//  @GetMapping("{id}")
//  public Mono<Response> getReactive(@PathVariable Long id) {
//    Mono<String> threeMono = webClient.get().uri("http://localhost:8083/" + id).retrieve().bodyToMono(String.class);
//    Mono<String> fourMono = webClient.get().uri("http://localhost:8084/" + id).retrieve().bodyToMono(String.class);
//    return threeMono.zipWith(fourMono, Response::new);
//  }

  public static class Response {
    public String three;
    public String four;

    public Response(String three, String four) {
      this.three = three;
      this.four = four;
    }
  }
}

//@Configuration
//class MyConfig {
//  @Bean
//  public WebClient webClient() {
//    return WebClient.create();
//  }
//}

