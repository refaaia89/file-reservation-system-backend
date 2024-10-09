package fifth.year.backendinternetapplication.service;

import fifth.year.backendinternetapplication.dto.request.JoinOrRemoveFromGroupRequest;
import fifth.year.backendinternetapplication.dto.request.create.GroupCreateRequest;
import fifth.year.backendinternetapplication.dto.request.update.GroupUpdateRequest;
import fifth.year.backendinternetapplication.dto.response.FileResponse;
import fifth.year.backendinternetapplication.dto.response.GroupResponse;
import fifth.year.backendinternetapplication.dto.response.fullData.FullGroupResponse;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.dto.response.main.SuccessResponse;
import fifth.year.backendinternetapplication.model.File;
import fifth.year.backendinternetapplication.model.Group;
import fifth.year.backendinternetapplication.model.User;
import fifth.year.backendinternetapplication.model.enums.RoleType;
import fifth.year.backendinternetapplication.repository.FileRepository;
import fifth.year.backendinternetapplication.repository.GroupRepository;
import fifth.year.backendinternetapplication.repository.UserRepository;
import fifth.year.backendinternetapplication.utils.FileUtil;
import fifth.year.backendinternetapplication.utils.validator.ObjectsValidator;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@Service
public class GroupService {

    private final AuthorizationService authorizationService;
    private final GroupRepository repository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final ObjectsValidator<GroupCreateRequest> groupCreateRequestObjectsValidator;
    private final ObjectsValidator<GroupUpdateRequest> groupUpdateRequestObjectsValidator;
    private final ObjectsValidator<JoinOrRemoveFromGroupRequest> joinOrRemoveFromGroupRequestObjectsValidator;


    public GroupService(AuthorizationService authorizationService, GroupRepository repository, UserRepository userRepository, FileRepository fileRepository, ObjectsValidator<GroupCreateRequest> groupCreateRequestObjectsValidator, ObjectsValidator<GroupUpdateRequest> groupUpdateRequestObjectsValidator, ObjectsValidator<JoinOrRemoveFromGroupRequest> joinOrRemoveFromGroupRequestObjectsValidator) {
        this.authorizationService = authorizationService;
        this.repository = repository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.groupCreateRequestObjectsValidator = groupCreateRequestObjectsValidator;
        this.groupUpdateRequestObjectsValidator = groupUpdateRequestObjectsValidator;
        this.joinOrRemoveFromGroupRequestObjectsValidator = joinOrRemoveFromGroupRequestObjectsValidator;
    }

    public ResponseEntity<Response> getAll(int page, int pageSize) {
        return ResponseEntity.ok(new SuccessResponse("Groups Retrieved successfully",
                repository.findAll(PageRequest.of(page, pageSize))
                        .stream()
                        .map(group -> new GroupResponse(group.getId(), group.getName(), group.getDescription(),
                                group.getAllowed_extension_file_types(), group.isIs_public(), group.getMax_members_count(),
                                group.getMax_files_count(), group.getMax_allowed_file_size_in_mb(),
                                group.getUser() == null ? null : group.getUser().getName(), group.getCreated_at(), group.getUpdated_at()))
                        .toList()));
    }

    public ResponseEntity<Response> getById(Long id) {
        return ResponseEntity.ok(new SuccessResponse("Group Data Retrieved successfully",
                repository.findById(id)
                        .map(FullGroupResponse::new)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group Not Found"))
        ));
    }

    public ResponseEntity<Response> create(GroupCreateRequest groupCreateRequest) {
        groupCreateRequestObjectsValidator.validate(groupCreateRequest);
        var group = new Group();
        group.setName(groupCreateRequest.name());
        group.setDescription(groupCreateRequest.description());
        group.setAllowed_extension_file_types(groupCreateRequest.allowed_extension_file_types());
        group.setIs_public(groupCreateRequest.is_public());
        group.setMax_files_count(groupCreateRequest.max_files_count());
        group.setMax_members_count(groupCreateRequest.max_members_count());
        group.setMax_allowed_file_size_in_mb(groupCreateRequest.max_allowed_file_size_in_mb());
        User user = userRepository.findById(groupCreateRequest.administrator_id()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The administrator sent to the request is not found")
        );
        group.setUser(user);
        group.setUsers(new HashSet<>(Set.of(user)));
        repository.save(group);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse("Group Created Successfully", null));
    }

    public ResponseEntity<Response> update(long id, GroupUpdateRequest groupUpdateRequest) {
        groupUpdateRequestObjectsValidator.validate(groupUpdateRequest);
        authorizationService.isAdminOfGroup(id);
        var group = repository.getReferenceById(id);
        if (groupUpdateRequest.name() != null) {
            group.setName(groupUpdateRequest.name());
        }
        if (groupUpdateRequest.description() != null) {
            group.setDescription(groupUpdateRequest.description());
        }
        if (groupUpdateRequest.allowed_extension_file_types() != null) {
            group.setAllowed_extension_file_types(groupUpdateRequest.allowed_extension_file_types());
        }
        if (groupUpdateRequest.is_public() != null) {
            group.setIs_public(groupUpdateRequest.is_public());
        }
        if (groupUpdateRequest.max_members_count() != null) {
            group.setMax_members_count(groupUpdateRequest.max_members_count());
        }
        if (groupUpdateRequest.max_files_count() != null) {
            group.setMax_files_count(groupUpdateRequest.max_files_count());
        }
        if (groupUpdateRequest.max_allowed_file_size_in_mb() != null) {
            group.setMax_allowed_file_size_in_mb(groupUpdateRequest.max_allowed_file_size_in_mb());
        }
        if (groupUpdateRequest.administrator_id() != null) {
            User user = userRepository.findById(groupUpdateRequest.administrator_id()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The administrator sent to the request is not found")
            );
            group.setUser(user);
        }
        repository.save(group);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("Group Updated Successfully", null));
    }

    public ResponseEntity<Response> deleteById(Long id) {
        authorizationService.isAdminOfGroup(id);
        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("Group Deleted Successfully", null));
    }

    public ResponseEntity<Response> findByNameContains(String name) {
        return ResponseEntity.ok(new SuccessResponse("Groups Retrieved successfully",
                repository.findByNameContains(name)
                        .stream()
                        .map(group -> new GroupResponse(group.getId(), group.getName(), group.getDescription(),
                                group.getAllowed_extension_file_types(), group.isIs_public(), group.getMax_members_count(),
                                group.getMax_files_count(), group.getMax_allowed_file_size_in_mb(),
                                group.getUser() == null ? null : group.getUser().getName(), group.getCreated_at(), group.getUpdated_at()))
                        .toList()));
    }

    public ResponseEntity<Response> getMyGroupFiles(Long id) {
        authorizationService.isMemberOfGroup(id);
        var u = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return ResponseEntity.ok(new SuccessResponse("Your Files in Group Retrieved successfully",
                fileRepository.findByUserIdAndGroupId(u.getId(), id).stream()
                        .map(file -> new FileResponse(file.getId(), file.getName(), file.isIsCheckedIn(), file.getChecked_in_until_time(),
                                file.getUser().getName(), file.getGroup().getName(),file.getUpdated_by(),  file.getCreated_at(), file.getUpdated_at()))
                        .toList()
                )
        );
    }

    @Transactional
    public ResponseEntity<Response> joinGroup(JoinOrRemoveFromGroupRequest joinGroupRequest) {
        joinOrRemoveFromGroupRequestObjectsValidator.validate(joinGroupRequest);
        List<Long> listGroup = getListOfJoinOrRemoveFromGroup(joinGroupRequest.group());

        listGroup.forEach(
                id -> accept(id, joinGroupRequest.user(), RequestMethod.POST)
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("Joining Group Done Successfully", null));
    }
    @Transactional
    public ResponseEntity<Response> leaveGroup(JoinOrRemoveFromGroupRequest leaveGroupRequest) {
        joinOrRemoveFromGroupRequestObjectsValidator.validate(leaveGroupRequest);
        List<Long> listGroup = getListOfJoinOrRemoveFromGroup(leaveGroupRequest.group());
        listGroup.forEach(
                id -> accept(id,leaveGroupRequest.user(), RequestMethod.DELETE)
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("Leaving Group Done Successfully", null));
    }

    public ResponseEntity<?> downloadFileFromGroup(long group_id,String fileName) throws IOException {
        authorizationService.isMemberOfGroup(group_id);
        File file = fileRepository.findByNameAndGroupId(fileName, group_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The File name sent with the request is not found")
        );
        Resource resource;
        resource = FileUtil.getFileAsResource(file.getGroup().getName(), fileName);
        if (resource == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The File name sent with the request is not found");
        }
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
    private void accept(Long groupId, Object userList, RequestMethod requestMethod) {
        Group group = repository.findById(groupId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Group id: " + groupId + " is not found")
        );
        List<Long> listUsers = new ArrayList<>();
        var u = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (!Objects.equals(u.getRole().getName(), RoleType.SUPER_ADMIN.getRole())
                && group.getUser() == null || !Objects.equals(u.getRole().getName(), RoleType.SUPER_ADMIN.getRole()) &&
                group.getUser() != null && !group.getUser().equals(u)) {
            listUsers.add(u.getId());
        } else {
            listUsers = getListOfJoinOrRemoveFromGroup(userList);
            if(listUsers.isEmpty())
                listUsers.add(u.getId());
        }
        listUsers.forEach(
                userId -> {
                    User user = userRepository.findById(userId).orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The User id: " + userId + " is not found")
                    );
                    boolean exists = group.getUsers().stream().anyMatch(us -> us.getId() == user.getId());
                    if (requestMethod == RequestMethod.POST) {
                        if (exists)
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user id: " + userId + " is already in the group id: " + groupId);
                        group.getUsers().add(user);
                    } else {
                        if (!exists)
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user id: " + userId + " is already not in the group id: " + groupId);
                        group.getUsers().remove(user);
                    }
                }
        );
        repository.save(group);
    }


    private List<Long> getListOfJoinOrRemoveFromGroup(Object data) {
        List<Long> list = new ArrayList<>();
        if (data instanceof Integer) {
            list.add(Long.valueOf((Integer) data));
        } else if (data instanceof ArrayList<?>) {
            List<?> dataList = (List<?>) data;
            dataList.forEach(
                    o -> {
                        if (o instanceof Integer) {
                            list.add(Long.valueOf((Integer) o));
                        } else {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Group Ids must all be true numbers");
                        }
                    }
            );

        }
        return list;
    }
}
