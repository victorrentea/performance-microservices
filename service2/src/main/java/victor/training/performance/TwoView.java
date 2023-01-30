package victor.training.performance;

public class TwoView {
  public Long id;
  public String name;
  public String threeSourceName;
  public String threeDestinationName;

  public TwoView(Long id, String name, String threeSourceName, String threeDestinationName) {
    this.id = id;
    this.name = name;
    this.threeSourceName = threeSourceName;
    this.threeDestinationName = threeDestinationName;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getThreeDestinationName() {
    return threeDestinationName;
  }

  public String getThreeSourceName() {
    return threeSourceName;
  }
}
