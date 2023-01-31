package victor.training.performance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
class RestTemplateOverHttpClient {
  @Autowired
  private RestTemplate customRestTemplate;

  @GetMapping("httpclient")
  public String method() {
    String data = customRestTemplate.getForObject("http://localhost:9999/fast20ms", String.class);
    return "result: " + data;
  }

  @Configuration
  public static class RestTemplateOverHttpClientConfig {
    @Bean
    public static RestTemplate customRestTemplate() {
      // copy-pasted from Stack Overflow (like a Pro)
      //  🛑 Using a large unknown framework without benchmarking it
      HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
      clientHttpRequestFactory.setConnectTimeout(5000);
      return new RestTemplate(clientHttpRequestFactory);
    }
  }


}

