package victor.training.performance;

import brave.sampler.Sampler;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cloud.sleuth.brave.sampler.ProbabilityBasedSampler;
import org.springframework.cloud.sleuth.brave.sampler.RateLimitingSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.io.IOException;


@Configuration
public class CommonConfig {
  @Bean
  public RestTemplate rest() {
    return new RestTemplate();
    // it is critical to define RestTemplate as a @Bean rather than instantiating it
    // at usage point, to allow Apache Sleuth to hack it to add request headers to propagate its Trace ID
  }

  @Bean // enables the use of @Timed on methods
  public TimedAspect timedAspect(MeterRegistry meterRegistry) {
    return new TimedAspect(meterRegistry);
  }

  @Bean
  public Sampler zipkinSampler() {
//    return new ProbabilityBasedSampler(() -> 1f); // kill production with observability -> brilliant
    return new RateLimitingSampler(() -> 10); // 100 / sec
  }

  @PreDestroy
  public void deleteGlowrootDatabase() throws IOException {
    GlowrootUtil.deleteDatabase();
  }

}
