package victor.training.performance.bar;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import victor.training.performance.MonitorQueueWaitingTime;

@Configuration
public class BarPoolConfig {
  @Bean
  public ThreadPoolTaskExecutor barPool(MeterRegistry meterRegistry, @Value("${bar.pool.size}") int barPoolSize) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(barPoolSize);
    executor.setMaxPoolSize(barPoolSize);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix("bar-");
    executor.setTaskDecorator(new MonitorQueueWaitingTime(meterRegistry.timer("barman-queue-time")));
    executor.initialize();
    return executor;
  }
}
