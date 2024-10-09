package fifth.year.backendinternetapplication.repository;

import fifth.year.backendinternetapplication.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log,Long> {
}
