package fifth.year.backendinternetapplication.repository;

import fifth.year.backendinternetapplication.model.Group;
import fifth.year.backendinternetapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group,Long> {
    Optional<Group> findByName(String name);

    List<Group> findGroupsByUsersId(Long userId);

    @Query(value = "SELECT g FROM Group g WHERE :user MEMBER OF g.users and g.id=:gid")
    Optional<Group> findGroupByUserId(User user, Long gid);

    List<Group> findByNameContains(String name);
}
