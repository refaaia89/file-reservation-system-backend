package fifth.year.backendinternetapplication.service;

import fifth.year.backendinternetapplication.dto.request.create.PermissionCreateRequest;
import fifth.year.backendinternetapplication.dto.request.update.PermissionUpdateRequest;
import fifth.year.backendinternetapplication.dto.response.PermissionResponse;
import fifth.year.backendinternetapplication.dto.response.fullData.FullPermissionResponse;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.dto.response.main.SuccessResponse;
import fifth.year.backendinternetapplication.model.Permission;
import fifth.year.backendinternetapplication.repository.PermissionRepository;
import fifth.year.backendinternetapplication.utils.validator.ObjectsValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PermissionService {
    private final PermissionRepository repository;

    private final ObjectsValidator<PermissionCreateRequest> permissionCreateRequestObjectsValidator;

    public PermissionService(PermissionRepository repository, ObjectsValidator<PermissionCreateRequest> permissionCreateRequestObjectsValidator) {
        this.repository = repository;
        this.permissionCreateRequestObjectsValidator = permissionCreateRequestObjectsValidator;
    }

    public ResponseEntity<Response> getAll(int page, int pageSize) {
        return ResponseEntity.ok(new SuccessResponse("Permissions Retrieved successfully",
                repository.findAll(PageRequest.of(page,pageSize))
                        .stream()
                        .map(permission -> new PermissionResponse(permission.getId(),permission.getName(),permission.getDescription(),
                                permission.getCreated_at(),permission.getUpdated_at()))
                        .toList()));
    }

    public ResponseEntity<Response> getById(Long id) {
        return ResponseEntity.ok(new SuccessResponse("Permission Data Retrieved successfully",
                repository.findById(id)
                        .map(FullPermissionResponse::new)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission Not Found"))
        ));
    }

    public ResponseEntity<Response> create(PermissionCreateRequest permissionCreateRequest) {
        permissionCreateRequestObjectsValidator.validate(permissionCreateRequest);
        var permission=new Permission();
        permission.setName(permissionCreateRequest.name());
        permission.setName(permissionCreateRequest.description());
        repository.save(permission);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse("Permission Created Successfully",null));
    }

    public ResponseEntity<Response> update(long id, PermissionUpdateRequest permissionUpdateRequest) {
        if (!repository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission Not Found");
        var permission = repository.getReferenceById(id);
        if(permissionUpdateRequest.name() != null) {
            permission.setName(permissionUpdateRequest.name());
        }
        if(permissionUpdateRequest.description() != null) {
            permission.setDescription(permissionUpdateRequest.description());
        }
        repository.save(permission);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("Permission Updated Successfully",null));
    }

    public ResponseEntity<Response> deleteById(Long id) {
        if (!repository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission Not Found");
        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("Permission Deleted Successfully",null));
    }
}