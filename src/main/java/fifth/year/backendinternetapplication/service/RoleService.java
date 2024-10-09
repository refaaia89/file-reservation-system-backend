package fifth.year.backendinternetapplication.service;

import fifth.year.backendinternetapplication.dto.request.create.RoleCreateRequest;
import fifth.year.backendinternetapplication.dto.response.RoleResponse;
import fifth.year.backendinternetapplication.dto.response.fullData.FullRoleResponse;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.dto.response.main.SuccessResponse;
import fifth.year.backendinternetapplication.model.Role;
import fifth.year.backendinternetapplication.repository.RoleRepository;
import fifth.year.backendinternetapplication.utils.validator.ObjectsValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RoleService {
    private final RoleRepository repository;
    private final ObjectsValidator<RoleCreateRequest> roleCreateRequestObjectsValidator;

    public RoleService(RoleRepository repository, ObjectsValidator<RoleCreateRequest> roleCreateRequestObjectsValidator) {
        this.repository = repository;
        this.roleCreateRequestObjectsValidator = roleCreateRequestObjectsValidator;
    }


    public ResponseEntity<Response> getAll(int page, int pageSize) {
        return ResponseEntity.ok(new SuccessResponse("Roles Retrieved successfully",
                repository.findAll(PageRequest.of(page,pageSize))
                        .stream()
                        .map(role -> new RoleResponse(role.getId(),role.getName(),role.getCreated_at(),role.getUpdated_at()))
                        .toList()));
    }

    public ResponseEntity<Response> getById(Long id) {
        return ResponseEntity.ok(new SuccessResponse("Role Data Retrieved successfully",
                repository.findById(id)
                        .map(FullRoleResponse::new)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role Not Found"))
        ));
    }

    public ResponseEntity<Response> create(RoleCreateRequest roleCreateRequest) {
        roleCreateRequestObjectsValidator.validate(roleCreateRequest);
        var role=new Role();
        role.setName(roleCreateRequest.name());
        repository.save(role);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse("Role Created Successfully",null));
    }

    public ResponseEntity<Response> update(long id, RoleCreateRequest roleUpdateRequest) {
        if (!repository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role Not Found");
        roleCreateRequestObjectsValidator.validate(roleUpdateRequest);
        var role = repository.getReferenceById(id);
        role.setName(roleUpdateRequest.name());
        repository.save(role);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("Role Updated Successfully",null));
    }

    public ResponseEntity<Response> deleteById(Long id) {
        if (!repository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role Not Found");
        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("Role Deleted Successfully",null));
    }
}
