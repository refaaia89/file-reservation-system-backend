package fifth.year.backendinternetapplication.controller;

import fifth.year.backendinternetapplication.dto.request.create.PermissionCreateRequest;
import fifth.year.backendinternetapplication.dto.request.update.PermissionUpdateRequest;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    private final PermissionService service;

    public PermissionController(PermissionService service) {
        this.service = service;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('permission:read')")
    public ResponseEntity<Response> getAll(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        return service.getAll(page - 1, pageSize);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:read')")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('permission:create')")
    public ResponseEntity<Response> create(@RequestBody PermissionCreateRequest permissionCreateRequest) {
        return service.create(permissionCreateRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:update')")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody PermissionUpdateRequest permissionUpdateRequest) {
        return service.update(id, permissionUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:delete')")
    public ResponseEntity<Response> deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }
}
