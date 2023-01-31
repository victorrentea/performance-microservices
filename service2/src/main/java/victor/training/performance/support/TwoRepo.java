package victor.training.performance.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;

public interface TwoRepo extends JpaRepository<Two, Long> {
  @Query("SELECT new victor.training.performance.support.TwoResultDto(c.id, c.name) FROM Two c " +
         "WHERE (:name is null OR UPPER(c.name) LIKE UPPER('%' || :name || '%'))" +
         "AND (:status is null OR c.status.name = :status)"
  )
  List<TwoResultDto> search(@Nullable @Param("name") String name, @Nullable @Param("status") String status);

}
