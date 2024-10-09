package fifth.year.backendinternetapplication.controller;


import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<Response> getAllCheckProcess() {
        return service.getAllCheckProcess();
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<Response> getAllCheckProcessByGroupId(
            @PathVariable Long id
    ) {
        return service.getAllCheckProcessByGroupId(id);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<Response> getAllCheckProcessByFileId(
            @PathVariable Long id
    ) {
        return service.getAllCheckProcessByFileId(id);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Response> getAllCheckProcessByUserId(
            @PathVariable Long id
    ) {
        return service.getAllCheckProcessByUserId(id);
    }

    @GetMapping("/checked-out-at-time")
    public ResponseEntity<Response> getAllCheckProcessByIsCheckedOutAtTimeIsTrued(
    ) {
        return service.getAllCheckProcessByIsCheckedOutAtTimeIsTrued();
    }
    @GetMapping("/not-checked-out-at-time")
    public ResponseEntity<Response> getAllCheckProcessByIsCheckedOutAtTimeIsFalse(
    ) {
        return service.getAllCheckProcessByIsCheckedOutAtTimeIsFalse();
    }

}
