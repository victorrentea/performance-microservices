package victor.training.performance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@EnableBinding(Source.class)
@RestController
@SpringBootApplication
public class Service4App {

  public static void main(String[] args) {
    SpringApplication.run(Service4App.class, args);
  }

  @Autowired
  private Source channel;

  @GetMapping("/service4/{id}")
  public String service4(@PathVariable Long id) {
    log.info("Inside zipkinService 4.. for id=" + id);
    log.info("Sending Message to Service 3");
    Message<String> message = MessageBuilder.withPayload("Hi From Service4!").build();
    channel.output().send(message);
    log.info("Message Sent");
    return "from4";
  }
}

