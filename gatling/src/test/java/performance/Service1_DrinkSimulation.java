package performance;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Service1_DrinkSimulation extends Simulation {

  {
    HttpProtocolBuilder httpProtocol = http
      .baseUrl("http://localhost:8081")
      .acceptHeader("*/*")
      .upgradeInsecureRequestsHeader("1")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");

    Map<CharSequence, String> headers = new HashMap<>();
    headers.put("Cache-Control", "max-age=0");
    headers.put("Proxy-Connection", "keep-alive");

    ScenarioBuilder scn = scenario(getClass().getSimpleName())
      .exec(http("drink").get("/drink/nonblocking/parallel").headers(Map.of()));
//      .exec(http("drink").get("/drink/nonblocking/parallel/unbounded").headers(Map.of()));

    setUp(scn.injectOpen(constantUsersPerSec(   1000).during(Duration.ofSeconds(5)))).protocols(httpProtocol);


//    setUp(
//            scn.injectOpen(
//                    // Pause for a given duration.
//                    nothingFor(4),
//
//                    //  Injects a given number of users at once.
//                    atOnceUsers(10),
//
//                    // Injects a given number of users distributed evenly on a time window of a given duration.
//                    rampUsers(10).during(5),
//
//                    // Injects users at a constant rate, defined in users per second,
//                    // during a given duration. Users will be injected at regular intervals.
//                    constantUsersPerSec(20).during(15),
//
//                    // Injects users at a constant rate, defined in users per second,
//                    // during a given duration. Users will be injected at randomized intervals.
//                    constantUsersPerSec(20).during(15).randomized(),
//
//                    // Injects users from starting rate to target rate, defined in users per second,
//                    // during a given duration. Users will be injected at regular intervals.
//                    rampUsersPerSec(10).to(20).during(10),
//
//                    // Injects a given number of users following a smooth approximation of the
//                    // heaviside step function stretched to a given duration.
//                    stressPeakUsers(1000).during(20)
//            ).protocols(httpProtocol)
//    );
  }
}
