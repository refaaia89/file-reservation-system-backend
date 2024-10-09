package fifth.year.backendinternetapplication.controller;

import fifth.year.backendinternetapplication.dto.request.JoinOrRemoveFromGroupRequest;
import fifth.year.backendinternetapplication.dto.request.create.GroupCreateRequest;
import fifth.year.backendinternetapplication.dto.request.update.GroupUpdateRequest;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('group:read')")
    public ResponseEntity<Response> getAll(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        return service.getAll(page - 1, pageSize);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('group:read')")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('group:create')")
    public ResponseEntity<Response> create(@RequestBody GroupCreateRequest groupCreateRequest) {
        return service.create(groupCreateRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('group:update')")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody GroupUpdateRequest groupUpdateRequest) {
        return service.update(id, groupUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('group:delete')")
    public ResponseEntity<Response> deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/searchByName")
    @PreAuthorize("hasAuthority('group:read')")
    public ResponseEntity<Response> findByNameContains(
            @RequestParam(value = "name", defaultValue = "") String name) {
        return service.findByNameContains(name);
    }

    @PostMapping("/join")
    @PreAuthorize("hasAuthority('group:read')")
    public ResponseEntity<Response> joinGroup(@RequestBody JoinOrRemoveFromGroupRequest joinOrRemoveFromGroupRequest) {
        return service.joinGroup(joinOrRemoveFromGroupRequest);
    }

    @PostMapping("/leave")
    @PreAuthorize("hasAuthority('group:read')")
    public ResponseEntity<Response> leaveGroup(@RequestBody JoinOrRemoveFromGroupRequest joinOrRemoveFromGroupRequest) {
        return service.leaveGroup(joinOrRemoveFromGroupRequest);
    }

    @GetMapping("/{id}/my-files")
    public ResponseEntity<Response> getMyGroupFiles(@PathVariable Long id) {
        return service.getMyGroupFiles(id);
    }

    @GetMapping("/{id}/download-file/{fileName}")
    @PreAuthorize("hasAuthority('file:read')")
    public ResponseEntity<?> downloadFileFromGroup(@PathVariable Long id, @PathVariable String fileName) throws IOException {
        return service.downloadFileFromGroup(id, fileName);
    }
}
