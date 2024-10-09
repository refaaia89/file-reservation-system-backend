package fifth.year.backendinternetapplication.repository;

import fifth.year.backendinternetapplication.model.File;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<File> findWithLockingById(Long id);
    Optional<File> findByNameAndGroupId(String name, long group_id);
    List<File> findByUserId(Long userId);
    List<File> findByUserIdAndIsCheckedInIsTrue(Long userId);
    List<File> findByUserIdAndGroupIdAndIsCheckedInIsTrue(Long userId,Long groupId);
    List<File> findByUserIdAndGroupId(Long userId, Long groupId);
    List<File> findByNameContains(String name);
    List<File> findByIsCheckedInIsTrue();
}
