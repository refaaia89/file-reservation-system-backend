package fifth.year.backendinternetapplication.service;

import fifth.year.backendinternetapplication.config.CheckInProperties;
import fifth.year.backendinternetapplication.dto.request.CheckInAndOutRequest;
import fifth.year.backendinternetapplication.dto.request.CopyFileFromGroupToAnother;
import fifth.year.backendinternetapplication.dto.request.create.FileCreateRequest;
import fifth.year.backendinternetapplication.dto.request.update.FileUpdateRequest;
import fifth.year.backendinternetapplication.dto.response.FileResponse;
import fifth.year.backendinternetapplication.dto.response.fullData.FullFileResponse;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.dto.response.main.SuccessResponse;
import fifth.year.backendinternetapplication.model.CheckProcess;
import fifth.year.backendinternetapplication.model.File;
import fifth.year.backendinternetapplication.model.Group;
import fifth.year.backendinternetapplication.model.User;
import fifth.year.backendinternetapplication.repository.CheckProcessRepository;
import fifth.year.backendinternetapplication.repository.FileRepository;
import fifth.year.backendinternetapplication.repository.GroupRepository;
import fifth.year.backendinternetapplication.repository.UserRepository;
import fifth.year.backendinternetapplication.utils.FileUtil;
import fifth.year.backendinternetapplication.utils.validator.ObjectsValidator;
import jakarta.transaction.Transactional;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileService {

    private final AuthorizationService authorizationService;
    private final FileRepository repository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final CheckProcessRepository checkProcessRepository;
    private final CheckInProperties checkInProperties;
    private final ObjectsValidator<FileCreateRequest> fileCreateRequestObjectsValidator;
    private final ObjectsValidator<FileUpdateRequest> fileUpdateRequestObjectsValidator;
    private final ObjectsValidator<CheckInAndOutRequest> checkInAndOutValidator;
    private final ObjectsValidator<CopyFileFromGroupToAnother> copyFileFromGroupToAnotherObjectsValidator;


    public FileService(AuthorizationService authorizationService, FileRepository repository, GroupRepository groupRepository, UserRepository userRepository, CheckProcessRepository checkProcessRepository, CheckInProperties checkInProperties, ObjectsValidator<FileCreateRequest> fileCreateRequestObjectsValidator, ObjectsValidator<FileUpdateRequest> fileUpdateRequestObjectsValidator, ObjectsValidator<CheckInAndOutRequest> checkInAndOutValidator, ObjectsValidator<CopyFileFromGroupToAnother> copyFileFromGroupToAnotherObjectsValidator) {
        this.authorizationService = authorizationService;
        this.repository = repository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.checkProcessRepository = checkProcessRepository;
        this.checkInProperties = checkInProperties;
        this.fileCreateRequestObjectsValidator = fileCreateRequestObjectsValidator;
        this.fileUpdateRequestObjectsValidator = fileUpdateRequestObjectsValidator;
        this.checkInAndOutValidator = checkInAndOutValidator;
        this.copyFileFromGroupToAnotherObjectsValidator = copyFileFromGroupToAnotherObjectsValidator;
    }

    public ResponseEntity<Response> getAll(int page, int pageSize) {
        return ResponseEntity.ok(new SuccessResponse("Files Retrieved successfully",
                repository.findAll(PageRequest.of(page, pageSize))
                        .stream()
                        .map(file -> new FileResponse(file.getId(), file.getName(), file.isIsCheckedIn(), file.getChecked_in_until_time(),
                                file.getUser() == null ? null : file.getUser().getName(), file.getGroup().getName(),file.getUpdated_by(),  file.getCreated_at(), file.getUpdated_at()))
                        .toList()));
    }

    public ResponseEntity<Response> getById(Long id) {
        return ResponseEntity.ok(new SuccessResponse("File Data Retrieved successfully",
                repository.findById(id)
                        .map(FullFileResponse::new)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File Not Found"))
        ));
    }

    public ResponseEntity<Response> create(FileCreateRequest fileCreateRequest) throws IOException {
        fileCreateRequestObjectsValidator.validate(fileCreateRequest);
        authorizationService.isMemberOfGroup(fileCreateRequest.group_id());
        var file = new File();
        Group group = groupRepository.findById(fileCreateRequest.group_id()).orElse(new Group());
        file.setName(fileCreateRequest.file().getOriginalFilename());
        file.setGroup(group);
        file.setUser(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        if (FileUtil.checkAllowedFile(group, fileCreateRequest.file(), RequestMethod.POST,fileCreateRequest.file().getOriginalFilename(),file.getId()))
            FileUtil.saveFile(group.getName(), fileCreateRequest.file());
        repository.save(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse("File Uploaded Successfully", null));
    }

    @Transactional
    public ResponseEntity<Response> update(long id, FileUpdateRequest fileUpdateRequest) throws IOException {
        fileUpdateRequestObjectsValidator.validate(fileUpdateRequest);
        authorizationService.isMemberOfGroupAndFileExists(id);
        var file = repository.findWithLockingById(id).orElse(new File());
        var uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        if(file.isIsCheckedIn() && file.getUpdated_by() != uid){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is checked by someone. You cant update it now");
        }
        boolean changeName = false;
        boolean changeGroup = false;
        String fileName = file.getName();
        String oldName = file.getName();
        Group oldGroup = file.getGroup();
        if (fileUpdateRequest.owner_id() != null) {
            User user = userRepository.findById(fileUpdateRequest.owner_id()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Owner sent to the request is not found")
            );
            file.setUser(user);
        }
        if (fileUpdateRequest.group_id() != null) {
            Group group = groupRepository.findById(fileUpdateRequest.group_id()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Group sent to the request is not found")
            );
            if (fileUpdateRequest.name() != null) {
                fileName = fileUpdateRequest.name() + "." +
                        (
                                fileUpdateRequest.file() != null ?
                                        FileUtil.getFileExtension(Objects.requireNonNull(fileUpdateRequest.file().getOriginalFilename()))
                                        :
                                        FileUtil.getFileExtension(file.getName())
                        );
            }
            else if (fileUpdateRequest.file() != null) {
                fileName = fileUpdateRequest.file().getOriginalFilename();
            }
            if (file.getGroup().getId() != group.getId() &&
                    FileUtil.checkAllowedFile(group, fileUpdateRequest.file(), RequestMethod.POST,fileName,file.getId())) {
                changeGroup = true;
            }
            file.setGroup(group);
        }
        if (fileUpdateRequest.name() != null) {
            fileName = fileUpdateRequest.name() + "." +
                    (
                            fileUpdateRequest.file() != null ?
                                    FileUtil.getFileExtension(Objects.requireNonNull(fileUpdateRequest.file().getOriginalFilename()))
                                    :
                                    FileUtil.getFileExtension(file.getName())
                    );
            FileUtil.checkAllowedFile(file.getGroup(), null, RequestMethod.PUT, fileName,file.getId());
        }
        if (fileUpdateRequest.file() != null) {
            if (fileUpdateRequest.name() == null) {
                fileName = fileUpdateRequest.file().getOriginalFilename();
            }
            if (FileUtil.checkAllowedFile(file.getGroup(), fileUpdateRequest.file(), RequestMethod.PUT, fileName,file.getId())) {
                String name = FileUtil.updateFile(oldGroup.getName(), fileUpdateRequest.file(), fileUpdateRequest.name(), oldName);
                if (fileUpdateRequest.name() == null) {
                    file.setName(name);
                }
            }
        }
        if (fileUpdateRequest.name() != null) {
            file.setName(
                    fileName
            );
            if (fileUpdateRequest.file() == null) {
                changeName = true;
            }
        }
        if (changeGroup) {
            FileUtil.moveFromTo(oldGroup.getName(), file.getGroup().getName(), fileUpdateRequest.file() != null ? file.getName() : oldName, file.getName());
        } else if (changeName) {
            FileUtil.renameTo(file.getGroup().getName(), oldName, file.getName());
        }
        repository.save(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("File Updated Successfully", null));
    }

    @Transactional
    public ResponseEntity<Response> deleteById(Long id) throws IOException {
        authorizationService.isAdminOfFile(id);
        File file = repository.getReferenceById(id);
        if(file.isIsCheckedIn()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is checked by someone. You cant delete it now");
        }
        repository.deleteById(id);
        FileUtil.deleteFile(file.getGroup().getName(), file.getName());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("File Deleted Successfully", null));
    }

    @Transactional
    public ResponseEntity<Response> copyFromGroupToAnother(CopyFileFromGroupToAnother copyFileFromGroupToAnother)
            throws IOException {
        copyFileFromGroupToAnotherObjectsValidator.validate(copyFileFromGroupToAnother);
        authorizationService.isAbleToCopyFromGroupToGroup(copyFileFromGroupToAnother.fileId(), copyFileFromGroupToAnother.groupId());
        File file = repository.getReferenceById(copyFileFromGroupToAnother.fileId());
        Group group = groupRepository.getReferenceById(copyFileFromGroupToAnother.groupId());
        FileUtil.copyFromTo(file.getGroup().getName(), group.getName(), file.getName(), file.getName());
        repository.save(new File(file.getName(), file.getUser(), group));
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("File Copied Successfully", null));
    }

    public ResponseEntity<Response> findByNameContains(String name) {
        return ResponseEntity.ok(new SuccessResponse("Files Retrieved successfully",
                repository.findByNameContains(name)
                        .stream()
                        .map(file -> new FileResponse(file.getId(), file.getName(), file.isIsCheckedIn(), file.getChecked_in_until_time(),
                                file.getUser() == null ? null : file.getUser().getName(), file.getGroup().getName(), file.getUpdated_by(), file.getCreated_at(), file.getUpdated_at()))
                        .toList()));
    }

    @Transactional
    public ResponseEntity<Response> checkIn(CheckInAndOutRequest data) {
        try {
            checkInAndOutValidator.validate(data);
            var filesList = getListOfCheckInOutFromFiles(data.files());
            filesList.forEach(this::acceptCheckIn);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse(filesList.size() == 1 ? "File Checked In Successfully" : "Files Checked In Successfully", null));
        } catch (LockAcquisitionException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Someone already reverse the file/s");
        }
    }

    @Transactional
    public ResponseEntity<Response> checkOut(CheckInAndOutRequest data) {
        checkInAndOutValidator.validate(data);
        var filesList = getListOfCheckInOutFromFiles(data.files());
        filesList.forEach(this::acceptCheckOut);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse(filesList.size() == 1 ? "File Checked Out Successfully" : "Files Checked Out Successfully", null));
    }

    private void acceptCheckIn(Long fileId) {
        File file = repository.findWithLockingById(fileId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The File id: " + fileId + " is not found")
        );
        var u = userRepository.findById(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).orElse(new User());
        authorizationService.isUserInGroup(u, file.getGroup());
        if (!file.isIsCheckedIn()) {
            file.setIsCheckedIn(true);
            file.setChecked_in_until_time(LocalDateTime.now().plusDays(checkInProperties.days()));
            checkProcessRepository.save(new CheckProcess(u, file));
        } else {
            if(file.getUpdated_by() == u.getId()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "You are already checked in this file No. " + file.getId());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The File id: " + fileId + " is already checked in by someone");
        }
        repository.save(file);
    }

    private void acceptCheckOut(Long fileId) {
        File file = repository.findById(fileId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The File id: " + fileId + " is not found")
        );
        var u = userRepository.findById(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).orElse(new User());
        authorizationService.isUserInGroup(u, file.getGroup());
        if (file.isIsCheckedIn()) {
            if (file.getUpdated_by() != u.getId()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is checked by someone else. You cant check it out");
            }
            List<CheckProcess> checkProcessesList = checkProcessRepository.findByFileAndCheckedOutAtIsNull(file);
            checkProcessesList.forEach(checkProcess ->
                    {
                        checkProcess.setCheckedOutAt(LocalDateTime.now());
                        checkProcess.setCheckedOutAtTime(true);
                        checkProcessRepository.save(checkProcess);
                    }
            );
            file.setIsCheckedIn(false);
            file.setChecked_in_until_time(null);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The File id: " + fileId + " is already not checked in by anyone");
        }
        repository.save(file);
    }

    private List<Long> getListOfCheckInOutFromFiles(Object data) {
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
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Files Ids must all be true numbers");
                        }
                    }
            );

        }
        return list;
    }
}
