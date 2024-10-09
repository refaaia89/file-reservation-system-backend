package fifth.year.backendinternetapplication.controller;

import fifth.year.backendinternetapplication.dto.request.create.RoleCreateRequest;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('role:read')")
    public ResponseEntity<Response> getAll(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        return service.getAll(page-1,pageSize);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:read')")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('role:create')")
    public ResponseEntity<Response> create(@RequestBody RoleCreateRequest roleCreateRequest) {
        return service.create(roleCreateRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody RoleCreateRequest roleUpdateRequest) {
        return service.update(id, roleUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:delete')")
    public ResponseEntity<Response> deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }
}