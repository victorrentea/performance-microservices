package victor.training.performance.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;

public interface TwoRepo extends JpaRepository<Two, Long> {
  @Query("SELECT c FROM Two c " +
         "WHERE (:name is null OR UPPER(c.name) LIKE UPPER('%' || :name || '%'))" +
         "AND (:status is null OR c.status.name = :status)"
  )
  List<Two> search(@Nullable String name, @Nullable String status);

}
