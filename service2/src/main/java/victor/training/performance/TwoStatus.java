package victor.training.performance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TwoStatus {
  @Id
  @GeneratedValue
  private Long id;
  private String name;

  public TwoStatus(String name) {
    this.name = name;
  }

  public TwoStatus() {
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public TwoStatus setId(Long id) {
    this.id = id;
    return this;
  }

  public TwoStatus setName(String name) {
    this.name = name;
    return this;
  }

  public String toString() {
    return "City(id=" + this.getId() + ", name=" + this.getName() + ")";
  }
}
