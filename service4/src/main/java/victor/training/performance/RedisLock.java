package victor.training.performance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RedisLock {
  private static final String MY_LOCK_KEY = "someLockKey";
  private final LockRegistry lockRegistry;


  @GetMapping("lock")
  public String properLock() throws InterruptedException {
    Lock lock;
    try {
      lock = lockRegistry.obtain(MY_LOCK_KEY);
    } catch (Exception e) {
      throw new RuntimeException("Cannot obtain lock: " + MY_LOCK_KEY, e);
    }
    try {
      //  🛑 childish/initial parameters ~> tune timeouts considering load to avoid OOME ~>
      if (lock.tryLock(1, MINUTES)) {
        log.info("ENTER critical section");
        sleep(3000);
        log.info("Perform critical action ☠️ ....");
        log.info("EXIT critical section");
        return "Critical action performed";
      } else {
        return "ERROR Could not obtain the lock in the given timeframe";
      }
    } finally {
      lock.unlock(); // DON'T FORGET THIS finally {
    }
  }

}
