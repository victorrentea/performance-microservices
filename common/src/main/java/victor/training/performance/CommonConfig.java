package victor.training.performance;

import brave.sampler.Sampler;
import org.springframework.cloud.sleuth.brave.sampler.RateLimitingSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class CommonConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate(); // it is critical to define RestTemplate as a @Bean rather than instantiating it
    // at usage point, to allow Apache Sleuth to hack it to add request headers to propagate its Trace ID
  }

  @Bean
  public Sampler zipkinSampler() {
    return new RateLimitingSampler(() -> 10); // 100 / sec
  }

  @PreDestroy
  public void deleteGlowrootDatabase() throws IOException {
    GlowrootUtil.deleteDatabase();
  }

}
