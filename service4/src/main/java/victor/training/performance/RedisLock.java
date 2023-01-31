package victor.training.performance;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;


class SomeData1KB {
  int[] data = new int[1024000/8];
}
@Slf4j
@RestController
@RequiredArgsConstructor
public class RedisLock {
  private static final String MY_LOCK_KEY = "someLockKey";
  private final LockRegistry lockRegistry;

  @GetMapping("lock")
  public String properLock() throws InterruptedException {
    SomeData1KB data = repoFindById();
    CompletableFuture.runAsync(() ->longRunningProcess(data)); // fire and forget method on ForkJoinPool
    // any thread pool has a queue. WHAT SIZE? idn ∞ ?!!

    // bad practice: call a method async oassing a large object kept in mem until the func runs, without putting a queue max size,
    return "Got your job ty!";
  }
  @SneakyThrows
  private void longRunningProcess(SomeData1KB data)  {
    Lock lock;
    try {
      lock = lockRegistry.obtain(MY_LOCK_KEY);
    } catch (Exception e) {
      throw new RuntimeException("Cannot obtain lock: " + MY_LOCK_KEY, e);
    }
    try {
      //  🛑 childish/initial parameters ~> tune timeouts considering load to avoid OOME ~>
      if (lock.tryLock(2, SECONDS)) { // how many threads at one point in time can be blocked here ?
        // hint: my machine has 10 CPUs =>
        // // TODO EXPECT 8 = (10-1)-1(that entered) - blocking the entire commonPool in JVM
        log.info("ENTER critical section");

        sleep(1000);
        log.info("Perform critical action ☠️ ....");
        log.info("EXIT critical section " + data);
//         "Critical action performed";
      } else {
//        return "ERROR Could not obtain the lock in the given timeframe";
      }
    } finally {
      lock.unlock(); // DON'T FORGET THIS finally {
    }
  }

  private SomeData1KB repoFindById() {
    return new SomeData1KB();
  }

}
