package fifth.year.backendinternetapplication.controller;

import fifth.year.backendinternetapplication.dto.request.CheckInAndOutRequest;
import fifth.year.backendinternetapplication.dto.request.CopyFileFromGroupToAnother;
import fifth.year.backendinternetapplication.dto.request.create.FileCreateRequest;
import fifth.year.backendinternetapplication.dto.request.update.FileUpdateRequest;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('file:read')")
    public ResponseEntity<Response> getAll(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        return service.getAll(page - 1, pageSize);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('file:read')")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping(value = "", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('file:create')")
    public ResponseEntity<Response> create(@ModelAttribute FileCreateRequest fileCreateRequest)
            throws IOException {
        return service.create(fileCreateRequest);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('file:update')")
    public ResponseEntity<Response> update(@PathVariable Long id, @ModelAttribute FileUpdateRequest fileUpdateRequest)
            throws IOException {
        return service.update(id, fileUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('file:delete')")
    public ResponseEntity<Response> deleteById(@PathVariable Long id) throws IOException {
        return service.deleteById(id);
    }

    @GetMapping("/searchByName")
    @PreAuthorize("hasAuthority('file:read')")
    public ResponseEntity<Response> findByNameContains(
            @RequestParam(value = "name", defaultValue = "") String name) {
        return service.findByNameContains(name);
    }

    @PostMapping("/check-in")
    @PreAuthorize("hasAuthority('file:update')")
    public ResponseEntity<Response> checkIn(@RequestBody CheckInAndOutRequest data) {
        return service.checkIn(data);
    }

    @PostMapping("/check-out")
    @PreAuthorize("hasAuthority('file:update')")
    public ResponseEntity<Response> checkOut(@RequestBody CheckInAndOutRequest data) {
        return service.checkOut(data);
    }

    @PostMapping("/copy-to")
    @PreAuthorize("hasAuthority('file:update')")
    public ResponseEntity<Response> joinGroup(@RequestBody CopyFileFromGroupToAnother copyFileFromGroupToAnother)
            throws IOException {
        return service.copyFromGroupToAnother(copyFileFromGroupToAnother);
    }
}
