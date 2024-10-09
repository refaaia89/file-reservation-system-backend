package fifth.year.backendinternetapplication.service;

import fifth.year.backendinternetapplication.dto.response.CheckProcessResponse;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.dto.response.main.SuccessResponse;
import fifth.year.backendinternetapplication.repository.CheckProcessRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final AuthorizationService authorizationService;
    private final CheckProcessRepository checkProcessRepository;
    public ReportService(AuthorizationService authorizationService, CheckProcessRepository checkProcessRepository) {
        this.authorizationService = authorizationService;
        this.checkProcessRepository = checkProcessRepository;
    }

    public ResponseEntity<Response> getAllCheckProcess() {
        authorizationService.isAdmin();
        var list = checkProcessRepository.findAll();
        return ResponseEntity.ok(new SuccessResponse("All Check Processes Retrieved successfully",
                list.stream().map(
                        (c) -> new CheckProcessResponse(
                                c.getId(), c.getCheckedInAt(), c.getCheckedOutAt(), c.isCheckedOutAtTime(), c.getUser(), c.getFile(), c.getCreated_at(), c.getUpdated_at()
                        )
                ).toList()));
    }


    public ResponseEntity<Response> getAllCheckProcessByGroupId(Long group_id) {
        authorizationService.isAdminOfGroup(group_id);
        var list = checkProcessRepository.findCheckProcessByGroupId(group_id);
        return ResponseEntity.ok(new SuccessResponse("All Check Processes to Group No. " + group_id + " Retrieved successfully",
                list.stream().map(
                        (c) -> new CheckProcessResponse(
                                c.getId(), c.getCheckedInAt(), c.getCheckedOutAt(), c.isCheckedOutAtTime(), c.getUser(), c.getFile(), c.getCreated_at(), c.getUpdated_at()
                        )
                ).toList()));
    }

    public ResponseEntity<Response> getAllCheckProcessByFileId(Long file_id) {
        authorizationService.isMemberOfGroupAndFileExists(file_id);
        var list = checkProcessRepository.findCheckProcessByFileId(file_id);
        return ResponseEntity.ok(new SuccessResponse("Check Processes Of File No. " + file_id + " Retrieved successfully",
                list.stream().map(
                        (c) -> new CheckProcessResponse(
                                c.getId(), c.getCheckedInAt(), c.getCheckedOutAt(), c.isCheckedOutAtTime(), c.getUser(), c.getFile(), c.getCreated_at(), c.getUpdated_at()
                        )
                ).toList()));
    }

    public ResponseEntity<Response> getAllCheckProcessByUserId(Long user_id) {
        var list = checkProcessRepository.findCheckProcessByUserId(user_id);
        return ResponseEntity.ok(new SuccessResponse("Check Processes Of User No. " + user_id + " Retrieved successfully",
                list.stream().map(
                        (c) -> new CheckProcessResponse(
                                c.getId(), c.getCheckedInAt(), c.getCheckedOutAt(), c.isCheckedOutAtTime(), c.getUser(), c.getFile(), c.getCreated_at(), c.getUpdated_at()
                        )
                ).toList()));
    }

    public ResponseEntity<Response> getAllCheckProcessByIsCheckedOutAtTimeIsTrued() {
        var list = checkProcessRepository.findCheckProcessByIsCheckedOutAtTimeIsTrue();
        return ResponseEntity.ok(new SuccessResponse("Check Processes that are checked out at time Retrieved successfully",
                list.stream().map(
                        (c) -> new CheckProcessResponse(
                                c.getId(), c.getCheckedInAt(), c.getCheckedOutAt(), c.isCheckedOutAtTime(), c.getUser(), c.getFile(), c.getCreated_at(), c.getUpdated_at()
                        )
                ).toList()));
    }

    public ResponseEntity<Response> getAllCheckProcessByIsCheckedOutAtTimeIsFalse() {
        var list = checkProcessRepository.findCheckProcessByIsCheckedOutAtTimeIsFalse();
        return ResponseEntity.ok(new SuccessResponse("Check Processes that are not checked out at time Retrieved successfully",
                list.stream().map(
                        (c) -> new CheckProcessResponse(
                                c.getId(), c.getCheckedInAt(), c.getCheckedOutAt(), c.isCheckedOutAtTime(), c.getUser(), c.getFile(), c.getCreated_at(), c.getUpdated_at()
                        )
                ).toList()));
    }
}
