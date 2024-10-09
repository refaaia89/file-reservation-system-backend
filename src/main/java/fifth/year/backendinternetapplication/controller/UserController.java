package fifth.year.backendinternetapplication.controller;

import fifth.year.backendinternetapplication.dto.request.ChangePasswordRequest;
import fifth.year.backendinternetapplication.dto.request.RegisterRequest;
import fifth.year.backendinternetapplication.dto.request.update.UserUpdateRequest;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.dto.response.main.SuccessResponse;
import fifth.year.backendinternetapplication.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<Response> getAll(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        return service.getAll((page - 1), pageSize);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<Response> create(@RequestBody RegisterRequest registerRequest) {
        return service.create(registerRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {
        return service.update(id, userUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<Response> deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @GetMapping("/searchByName")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<Response> findByNameContains(
            @RequestParam(value = "name", defaultValue = "") String name) {
        return service.findByNameContains(name);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest,
            Principal connectedUser) {
        service.changePassword(changePasswordRequest, connectedUser);
        return ResponseEntity.ok(new SuccessResponse(
                "Password Changed Successfully", null
        ));
    }

    @GetMapping("/profile")
    public ResponseEntity<Response> getMyAccountData() {
        return service.getMyAccountData();
    }

    @GetMapping("/groups")
    public ResponseEntity<Response> getMyGroups() {
        return service.getMyGroups();
    }

    @GetMapping("/files")
    public ResponseEntity<Response> getMyFiles() {
        return service.getMyFiles();
    }

    @GetMapping("/checked-in-files")
    public ResponseEntity<Response> getMyCheckedInFiles() {
        return service.getMyCheckedInFiles();
    }

    @GetMapping("/checked-in-files/group/{id}")
    public ResponseEntity<Response> getMyCheckedInGroupFiles(@PathVariable Long id) {
        return service.getMyCheckedInGroupFiles(id);
    }
}
