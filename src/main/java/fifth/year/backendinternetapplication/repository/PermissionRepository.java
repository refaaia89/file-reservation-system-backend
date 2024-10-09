package fifth.year.backendinternetapplication.repository;

import fifth.year.backendinternetapplication.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository  extends JpaRepository<Permission,Long> {
    Optional<Permission> findByName(String name);

}
