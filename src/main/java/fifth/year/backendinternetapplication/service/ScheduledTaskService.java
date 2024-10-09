package fifth.year.backendinternetapplication.service;

import fifth.year.backendinternetapplication.repository.CheckProcessRepository;
import fifth.year.backendinternetapplication.repository.FileRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScheduledTaskService {

    private final FileRepository fileRepository;
    private final CheckProcessRepository checkProcessRepository;

    public ScheduledTaskService(FileRepository fileRepository, CheckProcessRepository checkProcessRepository) {
        this.fileRepository = fileRepository;
        this.checkProcessRepository = checkProcessRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void execute() {
        fileRepository.findByIsCheckedInIsTrue().forEach(
                file -> {
                    if (file.getChecked_in_until_time().isBefore(LocalDateTime.now())) {
                        file.setIsCheckedIn(false);
                        file.setChecked_in_until_time(null);
                        file.getReservations().forEach(
                                checkProcess -> {
                                    if (checkProcess.getCheckedOutAt() == null) {
                                        checkProcess.setCheckedOutAt(LocalDateTime.now());
                                        checkProcess.setCheckedOutAtTime(false);
                                        checkProcessRepository.save(checkProcess);
                                    }
                                }
                        );
                        fileRepository.save(file);
                    }
                }
        );
    }
}