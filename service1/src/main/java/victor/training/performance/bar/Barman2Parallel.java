package victor.training.performance.bar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.lang.System.currentTimeMillis;

@RestController
@Slf4j
public class Barman2Parallel {
  @Autowired
  private RestTemplate rest;
  @Autowired
  private ThreadPoolTaskExecutor barPool;

  @GetMapping("/drink/parallel")
  public DillyDilly drink() throws ExecutionException, InterruptedException {
    long t0 = currentTimeMillis();
    Future<Beer> futureBeer = barPool.submit(() -> rest.getForObject("http://localhost:9999/beer", Beer.class));
    Future<Vodka> futureVodka = barPool.submit(() -> rest.getForObject("http://localhost:9999/vodka", Vodka.class));
    Beer beer = futureBeer.get();
    Vodka vodka = futureVodka.get();
    long t1 = currentTimeMillis();
    log.debug("HTTP thread blocked for millis: " + (t1 - t0));
    return new DillyDilly(beer, vodka);
  }
}


