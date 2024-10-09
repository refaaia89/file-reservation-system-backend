package fifth.year.backendinternetapplication.controller;


import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogController {
    private final LogService service;
    public LogController(LogService logService) {
        this.service = logService;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('log:read')")
    public ResponseEntity<Response> getAll(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        return service.getAll(page - 1, pageSize);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('log:read')")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        return service.getById(id);
    }
}
