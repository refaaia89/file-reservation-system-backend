package fifth.year.backendinternetapplication.service;

import fifth.year.backendinternetapplication.dto.request.ChangePasswordRequest;
import fifth.year.backendinternetapplication.dto.request.RegisterRequest;
import fifth.year.backendinternetapplication.dto.request.update.UserUpdateRequest;
import fifth.year.backendinternetapplication.dto.response.FileResponse;
import fifth.year.backendinternetapplication.dto.response.GroupResponse;
import fifth.year.backendinternetapplication.dto.response.UserResponse;
import fifth.year.backendinternetapplication.dto.response.fullData.FullUserResponse;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.dto.response.main.SuccessResponse;
import fifth.year.backendinternetapplication.model.Role;
import fifth.year.backendinternetapplication.model.User;
import fifth.year.backendinternetapplication.repository.FileRepository;
import fifth.year.backendinternetapplication.repository.GroupRepository;
import fifth.year.backendinternetapplication.repository.RoleRepository;
import fifth.year.backendinternetapplication.repository.UserRepository;
import fifth.year.backendinternetapplication.utils.validator.ObjectsValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Service
public class UserService {

    private final AuthorizationService authorizationService;
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final FileRepository fileRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectsValidator<RegisterRequest> registerValidator;
    private final ObjectsValidator<UserUpdateRequest> updateUserRequestObjectsValidator;
    private final ObjectsValidator<ChangePasswordRequest> changePasswordValidator;

    public UserService(AuthorizationService authorizationService, UserRepository repository, RoleRepository roleRepository, GroupRepository groupRepository, FileRepository fileRepository, PasswordEncoder passwordEncoder, ObjectsValidator<RegisterRequest> registerValidator, ObjectsValidator<UserUpdateRequest> updateUserRequestObjectsValidator, ObjectsValidator<ChangePasswordRequest> changePasswordValidator) {
        this.authorizationService = authorizationService;
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        this.fileRepository = fileRepository;
        this.passwordEncoder = passwordEncoder;
        this.registerValidator = registerValidator;
        this.updateUserRequestObjectsValidator = updateUserRequestObjectsValidator;
        this.changePasswordValidator = changePasswordValidator;
    }


    public ResponseEntity<Response> getAll(int page, int pageSize) {
        return ResponseEntity.ok(new SuccessResponse("Users Retrieved successfully",
                repository.findAll(PageRequest.of(page, pageSize))
                        .stream()
                        .map(user -> new UserResponse(
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getRole() == null ? null : user.getRole().getName(),
                                user.getCreated_at(),
                                user.getUpdated_at()
                        ))
                        .toList()));
    }

    public ResponseEntity<Response> getById(Long id) {
        return ResponseEntity.ok(new SuccessResponse("User Data Retrieved successfully",
                repository.findById(id)
                        .map(FullUserResponse::new)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"))
        ));
    }

    public ResponseEntity<Response> create(RegisterRequest registerRequest) {
        registerValidator.validate(registerRequest);
        var user = new User();
        userCreate(registerRequest, user, passwordEncoder, roleRepository);
        repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse("User Created Successfully", null));
    }

    public ResponseEntity<Response> update(long id, UserUpdateRequest userUpdateRequest) {
        updateUserRequestObjectsValidator.validate(userUpdateRequest);
        var user = repository.getReferenceById(id);
        if (userUpdateRequest.email() != null) {
            user.setEmail(userUpdateRequest.email());
        }
        if (userUpdateRequest.username() != null) {
            user.setName(userUpdateRequest.username());
        }
        if (userUpdateRequest.password() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateRequest.password()));
        }
        if (userUpdateRequest.role_id() != null) {
            Role role = roleRepository.findById(userUpdateRequest.role_id()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The role sent to the request is not found")
            );
            user.setRole(role);
        }
        repository.save(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("User Updated Successfully", null));
    }

    public ResponseEntity<Response> deleteById(Long id) {
        if (!repository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("User Deleted Successfully", null));
    }

    public ResponseEntity<Response> findByNameContains(String name) {
        return ResponseEntity.ok(new SuccessResponse("Users Retrieved successfully",
                repository.findByNameContains(name)
                        .stream()
                        .map(user -> new UserResponse(
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getRole() == null ? null : user.getRole().getName(),
                                user.getCreated_at(),
                                user.getUpdated_at()
                        )).toList()));
    }

    public ResponseEntity<Response> getMyAccountData() {
        var u = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return ResponseEntity.ok(new SuccessResponse("Your Profile Information Retrieved successfully",
                repository.findById(u.getId())
                        .map(FullUserResponse::new)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"))
                )
        );
    }

    public ResponseEntity<Response> getMyGroups() {
        var u = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return ResponseEntity.ok(new SuccessResponse("Your Groups Retrieved successfully",
                groupRepository.findGroupsByUsersId(u.getId())
                        .stream()
                        .map(group -> new GroupResponse(group.getId(), group.getName(), group.getDescription(),
                                group.getAllowed_extension_file_types(), group.isIs_public(), group.getMax_members_count(),
                                group.getMax_files_count(), group.getMax_allowed_file_size_in_mb(),
                                group.getUser().getName(), group.getCreated_at(), group.getUpdated_at()))
                        .toList()));
    }

    public ResponseEntity<Response> getMyFiles() {
        var u = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return ResponseEntity.ok(new SuccessResponse("Your Files Retrieved successfully",
                fileRepository.findByUserId(u.getId())
                        .stream()
                        .map(file -> new FileResponse(file.getId(), file.getName(), file.isIsCheckedIn(), file.getChecked_in_until_time(),
                                file.getUser().getName(), file.getGroup().getName(),file.getUpdated_by(),  file.getCreated_at(), file.getUpdated_at()))
                        .toList()));
    }

    public ResponseEntity<Response> getMyCheckedInFiles() {
        var u = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return ResponseEntity.ok(new SuccessResponse("Your Checked In Files Retrieved successfully",
                fileRepository.findByUserIdAndIsCheckedInIsTrue(u.getId())
                        .stream()
                        .map(file -> new FileResponse(file.getId(), file.getName(), file.isIsCheckedIn(), file.getChecked_in_until_time(),
                                file.getUser().getName(), file.getGroup().getName(),file.getUpdated_by(),  file.getCreated_at(), file.getUpdated_at()))
                        .toList()));
    }

    public ResponseEntity<Response> getMyCheckedInGroupFiles(Long group_id) {
        authorizationService.isMemberOfGroup(group_id);
        var u = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return ResponseEntity.ok(new SuccessResponse("Your Checked In Files In Group No. "+group_id+" Retrieved successfully",
                fileRepository.findByUserIdAndGroupIdAndIsCheckedInIsTrue(u.getId(),group_id)
                        .stream()
                        .map(file -> new FileResponse(file.getId(), file.getName(), file.isIsCheckedIn(), file.getChecked_in_until_time(),
                                file.getUser().getName(), file.getGroup().getName(),file.getUpdated_by(),  file.getCreated_at(), file.getUpdated_at()))
                        .toList()));
    }


    public void changePassword(ChangePasswordRequest changePasswordRequest, Principal connectedUser) {
        changePasswordValidator.validate(changePasswordRequest);
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(changePasswordRequest.current_password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");
        }
        if (!changePasswordRequest.new_password().equals(changePasswordRequest.confirmation_password())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords are not the same");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.new_password()));
        repository.save(user);
    }


    static void userCreate(RegisterRequest registerRequest, User user, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        user.setName(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        Role role = roleRepository.findByName("User").orElse(null);
        user.setRole(role);
    }
}
