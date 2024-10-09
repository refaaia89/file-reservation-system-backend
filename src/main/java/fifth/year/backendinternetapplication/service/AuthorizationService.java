package fifth.year.backendinternetapplication.service;

import fifth.year.backendinternetapplication.model.File;
import fifth.year.backendinternetapplication.model.Group;
import fifth.year.backendinternetapplication.model.User;
import fifth.year.backendinternetapplication.model.enums.RoleType;
import fifth.year.backendinternetapplication.repository.FileRepository;
import fifth.year.backendinternetapplication.repository.GroupRepository;
import fifth.year.backendinternetapplication.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthorizationService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final FileRepository fileRepository;

    public AuthorizationService(UserRepository userRepository, GroupRepository groupRepository, FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.fileRepository = fileRepository;
    }


    public void isAdminOfGroup(Long group_id) {
        Optional<Group> groupC = groupRepository.findById(group_id);
        if (groupC.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group Not Found");
        var u = userRepository.findById(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).orElse(new User());
        if (!Objects.equals(u.getRole().getName(), RoleType.SUPER_ADMIN.getRole()) &&
                (groupC.get().getUser() != null && !groupC.get().getUser().equals(u)))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "you are not an admin to that Group");
    }

    public void isMemberOfGroup(Long group_id) {
        Optional<Group> groupC = groupRepository.findById(group_id);
        if (groupC.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group Not Found");
        var u = userRepository.findById(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).orElse(new User());
        if (!Objects.equals(u.getRole().getName(), RoleType.SUPER_ADMIN.getRole()) &&
                groupRepository.findGroupByUserId(u, group_id).isEmpty() && !groupC.get().isIs_public())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "you are not in that Group");
    }

    public void isMemberOfGroupAndFileExists(Long file_id) {
        Optional<File> fileOptionalC = fileRepository.findById(file_id);
        if (fileOptionalC.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File Not Found");
        isMemberOfGroup(fileOptionalC.get().getGroup().getId());
    }

    public void isAdminOfFile(Long file_id) {
        Optional<File> fileOptionalC = fileRepository.findById(file_id);
        if (fileOptionalC.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File Not Found");
        var u = userRepository.findById(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).orElse(new User());
        if (!Objects.equals(u.getRole().getName(), RoleType.SUPER_ADMIN.getRole()) &&
                !fileOptionalC.get().getUser().equals(u) &&
                !fileOptionalC.get().getGroup().getUser().equals(u))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you are not an admin to that file");
    }

    public void isAbleToCopyFromGroupToGroup(Long file_id, Long group_id) {
        Optional<File> fileOptionalC = fileRepository.findById(file_id);
        if (fileOptionalC.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File Not Found");
        var u = userRepository.findById(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).orElse(new User());
        isUserInGroup(u, fileOptionalC.get().getGroup());
        Optional<Group> groupOptional = groupRepository.findById(group_id);
        if (groupOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group Not Found");
        if (!Objects.equals(u.getRole().getName(), RoleType.SUPER_ADMIN.getRole()) &&
                groupOptional.get().getUsers().stream().noneMatch(us -> us.getId() == u.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you are not in the group you want to copy the file to");
    }

    public void isUserInGroup(User u, Group g){
        if (!Objects.equals(u.getRole().getName(), RoleType.SUPER_ADMIN.getRole()) &&
                g.getUsers().stream().noneMatch(us -> us.getId() == u.getId())
                && !g.isIs_public())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you are not in the group that contains the file");
    }

    public void isAdmin(){
        var u = userRepository.findById(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).orElse(new User());
        if (!Objects.equals(u.getRole().getName(), RoleType.SUPER_ADMIN.getRole()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you are not allowed to do this action");
    }
}
