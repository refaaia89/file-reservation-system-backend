package fifth.year.backendinternetapplication.repository;

import fifth.year.backendinternetapplication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(String name);
}
