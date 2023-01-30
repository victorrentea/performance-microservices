package victor.training.performance.bar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static java.lang.System.currentTimeMillis;

@RestController
@Slf4j
public class Barman1Sequential {
  @Autowired
  private RestTemplate rest;

  @GetMapping("/drink/sequential")
  public DillyDilly drink() {
    long t0 = currentTimeMillis();
    Beer beer = rest.getForObject("http://localhost:9999/beer", Beer.class);
    Vodka vodka = rest.getForObject("http://localhost:9999/vodka", Vodka.class);
    long t1 = currentTimeMillis();
    log.debug("HTTP thread blocked for millis: " + (t1 - t0));
    return new DillyDilly(beer,vodka);
  }
}
