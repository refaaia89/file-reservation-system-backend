package fifth.year.backendinternetapplication.repository;

import fifth.year.backendinternetapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    List<User> findByNameContains(String name);
}
