import io.gatling.recorder.GatlingRecorder;
import io.gatling.recorder.config.RecorderPropertiesBuilder;
import lombok.val;
import scala.Some;

public class RecorderJava {

  public static void main(String[] args) {

    var props = new RecorderPropertiesBuilder()
            .simulationsFolder(IDEPathHelperJava.mavenSourcesDirectory().toString())
            .resourcesFolder(IDEPathHelperJava.mavenResourcesDirectory().toString())
            .simulationPackage("performance");

    GatlingRecorder.fromMap(props.build(), Some.apply(IDEPathHelperJava.recorderConfigFile()));

  }
}
