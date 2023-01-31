package performance;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static java.time.Duration.ofSeconds;

public class Service2_SearchSimulation extends Simulation {
  {
    // TODO simplify restu
    HttpProtocolBuilder httpProtocol = http
      .baseUrl("http://localhost:8082")
      .acceptHeader("*/*")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");

    ScenarioBuilder scn = scenario(getClass().getSimpleName())
      .exec(http("search").get("/search?name=n"));

    setUp(scn.injectClosed(constantConcurrentUsers(40).during(ofSeconds(5)))).protocols(httpProtocol);
  }
}
