package performance;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Service1_GetByIdSimulation extends Simulation {

  {
    HttpProtocolBuilder httpProtocol = http
      .baseUrl("http://localhost:8081")
      .acceptHeader("*/*")
      .upgradeInsecureRequestsHeader("1")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");

    Map<CharSequence, String> headers_0 = new HashMap<>();
    headers_0.put("Cache-Control", "max-age=0");
    headers_0.put("Proxy-Connection", "keep-alive");

    ScenarioBuilder scn = scenario(getClass().getSimpleName())
      .exec(
        http("getById")
          .get("/1")
          .headers(headers_0)
      );

    setUp(scn.injectOpen(constantUsersPerSec(1000).during(Duration.ofSeconds(10)))).protocols(httpProtocol);
  }
}
