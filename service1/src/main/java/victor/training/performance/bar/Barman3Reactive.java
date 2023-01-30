package victor.training.performance.bar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class Barman3Reactive {
  @Autowired
  private WebClient webClient;

  @GetMapping("/drink/reactive")
  public Mono<DillyDilly> drink() {
    Mono<Beer> futureBeer = webClient.get().uri("http://localhost:9999/beer").retrieve().bodyToMono(Beer.class);
    Mono<Vodka> futureVodka = webClient.get().uri("http://localhost:9999/vodka").retrieve().bodyToMono(Vodka.class);
    return futureBeer.zipWith(futureVodka, DillyDilly::new);
  }
}


