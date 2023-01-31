package victor.training.performance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.performance.support.Two;
import victor.training.performance.support.TwoRepo;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
public class Search {
  @Autowired
  private TwoRepo repo;

  private final Map<String, Deque<String>> lastNamesSearchedByStatus = Collections.synchronizedMap(new HashMap<>());

  @GetMapping("/search")

  public synchronized List<String> search(
          //  🛑 Lock contention ~> reduce the size of the protected critical section
          @RequestParam(required = false) String name,
          @RequestParam(required = false) String status) {
    if (status != null) {
      Deque<String> deque = lastNamesSearchedByStatus.computeIfAbsent(status, k -> new LinkedList<>());
      deque.add(name);
      if (deque.size() > 10) deque.removeLast();
    }

    // 🛑 SELECTing full entities for search with ORM ~> "select new Dto"
    return repo.search(name, status)
            .stream().map(Two::getName).collect(toList());//
  }

}
