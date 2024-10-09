package fifth.year.backendinternetapplication.repository;

import fifth.year.backendinternetapplication.model.CheckProcess;
import fifth.year.backendinternetapplication.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CheckProcessRepository extends JpaRepository<CheckProcess,Long> {
    List<CheckProcess> findByFileAndCheckedOutAtIsNull(File file);

    @Query("select c from CheckProcess c where c.file.group.id = :group_id")
    List<CheckProcess> findCheckProcessByGroupId(long group_id);

    List<CheckProcess> findCheckProcessByFileId(long file_id);

    List<CheckProcess> findCheckProcessByUserId(long user_id);

    List<CheckProcess> findCheckProcessByIsCheckedOutAtTimeIsTrue();

    List<CheckProcess> findCheckProcessByIsCheckedOutAtTimeIsFalse();
}
