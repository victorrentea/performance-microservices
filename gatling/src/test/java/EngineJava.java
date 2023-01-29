import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;

public class EngineJava {
  public static void main(String[] args) {

    GatlingPropertiesBuilder props = new GatlingPropertiesBuilder()
            .resourcesDirectory(IDEPathHelperJava.mavenResourcesDirectory().toString())
            .resultsDirectory(IDEPathHelperJava.resultsDirectory().toString())
            .binariesDirectory(IDEPathHelperJava.mavenBinariesDirectory().toString());

    Gatling.fromMap(props.build());
  }
}
