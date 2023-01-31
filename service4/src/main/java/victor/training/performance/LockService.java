package victor.training.performance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;

import static java.lang.Thread.sleep;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LockService {
  private static final String MY_LOCK_KEY = "someLockKey";
  private final LockRegistry lockRegistry;


  @GetMapping("lock")
  public String properLock() {
    Lock lock = null;
    try {
      lock = lockRegistry.obtain(MY_LOCK_KEY);
    } catch (Exception e) {
      // in a production environment this should be a log statement
      System.out.println(String.format("Unable to obtain lock: %s", MY_LOCK_KEY));
    }
    String returnVal = null;
    try {
      if (lock.tryLock()) {
        returnVal =  "jdbc lock successful";
        log.info("Enter critical section");
        sleep(3000);
        log.info("EXIT critical section");
      }
      else{
        returnVal = "jdbc lock unsuccessful";
      }

    } catch (Exception e) {
      // in a production environment this should log and do something else
      e.printStackTrace();
    } finally {
      // always have this in a `finally` block in case anything goes wrong
      lock.unlock();
    }

    return returnVal;
  }

}
