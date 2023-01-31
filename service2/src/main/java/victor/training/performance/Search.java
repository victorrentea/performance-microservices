package victor.training.performance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.performance.support.TwoRepo;
import victor.training.performance.support.TwoResultDto;

import java.util.*;

@RestController
public class Search {
  @Autowired
  private TwoRepo repo;

  // there was NO elastic scaling. Just ONE BIG INSTANCE
  private final Map<String, Deque<String>> last10Searches = Collections.synchronizedMap(new HashMap<>());
// TODO deadlock
  //  private final Map<String, Deque<String>> bMap = Collections.synchronizedMap(new HashMap<>());
//
//  public void zmartDevFPManiac() {
//    aMap.computeIfAbsent("", k-> networkCall());
//  }
//
//  public Deque<String> networkCall() {
//    bMap.computeIfAbsent("", k-> aMap.get("`BUUUUM"));
//  }

  @GetMapping("/search")
  public List<TwoResultDto> search(
          //  🛑 Lock contention ~> reduce the size of the protected critical section
          @RequestParam(required = false) String name,
          @RequestParam(required = false) String status) throws InterruptedException {
    if (status != null) {
      synchronized(last10Searches) {
        Deque<String> queue = last10Searches.computeIfAbsent(status, k -> new LinkedList<>());
        if (queue.size() == 10) {
          queue.removeLast();
        }
        //      Thread.sleep(10);// race condition that manifests when you sleep here.
        queue.add(name);
      }
      // #1 size=10
      // #2 size=11 Game Over : user reports bug that it takes 1w of hard work to reproduce. WHy did I became a programmer ?!!
    }

    // 🛑 SELECTing full entities for search with ORM ~> "select new Dto"
    return repo.search(name, status);
  }

}
