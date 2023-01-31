package performance;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static java.time.Duration.ofSeconds;

public class Service4_LockSimulation extends Simulation {
  {
    HttpProtocolBuilder httpProtocol = http
      .baseUrl("http://localhost:8084")
      .acceptHeader("*/*")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");

    ScenarioBuilder scn = scenario(getClass().getSimpleName())
      .exec(http("lock").get("/lock"));

    setUp(scn.injectClosed(constantConcurrentUsers(40).during(ofSeconds(5)))).protocols(httpProtocol);
  }
}