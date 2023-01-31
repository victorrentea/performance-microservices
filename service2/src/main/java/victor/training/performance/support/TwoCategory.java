package victor.training.performance.support;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TwoCategory {
  @Id
  @GeneratedValue
  private Long id;
  private String name;

  public TwoCategory(String name) {
    this.name = name;
  }

  public TwoCategory() {
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public TwoCategory setId(Long id) {
    this.id = id;
    return this;
  }

  public TwoCategory setName(String name) {
    this.name = name;
    return this;
  }

  public String toString() {
    return "Category(id=" + getId() + ", name=" + getName() + ")";
  }
}
