package victor.training.performance.support;

public class TwoResultDto {
  private final Long id;
  private final String name;

  public TwoResultDto(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }
}
