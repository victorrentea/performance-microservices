package victor.training.performance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
class FineTunedRestTemplate {
  @Autowired
  private RestTemplate customRestTemplate;

  @GetMapping("bottle")
  public String method() {
    return "result: " +customRestTemplate.getForObject("http://localhost:8083/fast", String.class);
  }
}
