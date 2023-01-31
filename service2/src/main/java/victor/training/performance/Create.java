package victor.training.performance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import victor.training.performance.support.Two;
import victor.training.performance.support.TwoRepo;

@RestController
public class Create {
  @Autowired
  private TwoRepo repo;
  @Autowired
  private RestTemplate rest;

  @PostMapping("{name}")
  @Transactional
  public String create(@PathVariable String name) {
    //  🛑 REST GET in transaction blocking DB connection ~> pull it out of transaction (here: delete @Transactional)
    String four = rest.getForObject("http://localhost:9999/fast5ms", String.class);

    Two two = new Two(name);
    repo.save(two.setFour(four));

    //  🛑 using REST for notifications ~> Use a queue
    rest.postForObject("http://localhost:8083/send-email/"+two.getId(), null, String.class);
    return "Ok";
  }
}
