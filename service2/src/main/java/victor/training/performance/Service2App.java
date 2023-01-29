package victor.training.performance;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.List;

import static javax.persistence.CascadeType.PERSIST;

@Slf4j
@RestController
@SpringBootApplication
public class Service2App {
  public static void main(String[] args) {
    SpringApplication.run(Service2App.class, args);
  }

  @Autowired
  private RestTemplate rest;

  @Autowired
  private CustomerRepo repo;

  @GetMapping("/service2")
  public String service2() throws InterruptedException {
    log.info("Start service 2...");

    log.info("Now let's simulate working with Files, DB query, CPU task...");
    Thread.sleep(80);
    log.info("Long job done...");

    log.info("Calling service3");
    String response = rest.getForObject("http://localhost:8083/service3", String.class);
    log.info("Got response from service3");
    return "from2 " + response;
  }

  @GetMapping("/search")
  public List<Customer> search(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String city) {
    return repo.search(name, city);
  }

  @EventListener(ApplicationStartedEvent.class)
  public void insertDummyData() {
    for (int i = 0; i < 20; i++) {
      repo.save(
              new Customer("John Doe" + i)
                      .setCity(new City("Bucharest" + i))
                      .setCategory(new Category("Category" + i)));
    }
  }
}

@Getter
@Setter
@NoArgsConstructor
@Entity
class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  @ManyToOne(cascade = PERSIST) // 4test
  private City city;
  @ManyToOne(cascade = PERSIST) // 4test
  private Category category;

  public Customer(String name) {
    this.name = name;
  }
}

interface CustomerRepo extends JpaRepository<Customer, Long> {
  @Query("SELECT c FROM Customer c " +
         "WHERE (:name is null OR UPPER(c.name) LIKE UPPER('%' || :name || '%'))" +
         "AND (:cityName is null OR c.city.name = :cityName)"
        )
  List<Customer> search(@Nullable String name, @Nullable String cityName);

}

@Data
@Entity
@NoArgsConstructor
class City {
  @Id
  @GeneratedValue
  private Long id;
  private String name;

  public City(String name) {
    this.name = name;
  }
}

@Data
@Entity
@NoArgsConstructor
class Category {
  @Id
  @GeneratedValue
  private Long id;
  private String name;

  public Category(String name) {
    this.name = name;
  }
}

