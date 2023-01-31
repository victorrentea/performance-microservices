package victor.training.performance.support;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import static javax.persistence.CascadeType.PERSIST;

@Entity
public class Two {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String four;
  private Long threeSourceId;
  private Long threeDestinationId;
  @ManyToOne(cascade = PERSIST) // 4test
  private TwoStatus status;
  @ManyToOne(cascade = PERSIST) // 4test
  private TwoCategory category;

  public Two(String name) {
    this.name = name;
  }


  public String getFour() {
    return four;
  }

  public Two setFour(String four) {
    this.four = four;
    return this;
  }

  public Two() {
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public TwoStatus getStatus() {
    return this.status;
  }

  public TwoCategory getCategory() {
    return this.category;
  }

  public Two setName(String name) {
    this.name = name;
    return this;
  }

  public Two setStatus(TwoStatus city) {
    this.status = city;
    return this;
  }

  public Two setCategory(TwoCategory category) {
    this.category = category;
    return this;
  }

  public Long getThreeDestinationId() {
    return threeDestinationId;
  }

  public Long getThreeSourceId() {
    return threeSourceId;
  }

  public Two setId(Long id) {
    this.id = id;
    return this;
  }

  public Two setThreeDestinationId(Long threeDad) {
    this.threeDestinationId = threeDad;
    return this;
  }

  public Two setThreeSourceId(Long threeMom) {
    this.threeSourceId = threeMom;
    return this;
  }
}
