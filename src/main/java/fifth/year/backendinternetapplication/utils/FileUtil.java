package fifth.year.backendinternetapplication.utils;

import fifth.year.backendinternetapplication.model.Group;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class FileUtil {
    private static Path foundFile;
    public static Resource getFileAsResource(String groupName, String fileName) throws IOException {
        Path dirPath = Paths.get("Files-Storage/" + groupName);

        if (!Files.exists(dirPath)) {
            throw new IOException("Could not find file: " + fileName);
        }
        foundFile=null;
        try (Stream<Path> paths = Files.list(dirPath)) {
            paths.forEach(file -> {
                if (file.getFileName().toString().equals(fileName)) {
                    foundFile = file;
                }
            });
        }
        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }
        return null;
    }

    public static void saveFile(String dir, MultipartFile multipartFile)
            throws IOException {

        Path uploadPath = Paths
                .get("Files-Storage/" + dir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + multipartFile.getOriginalFilename(), ioe);
        }
    }

    public static String updateFile(String dir, MultipartFile multipartFile, String newName, String oldName)
            throws IOException {

        Path uploadPath = Paths
                .get("Files-Storage/" + dir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        if (newName == null) {
            newName = multipartFile.getOriginalFilename();
        } else {
            newName = newName.concat("." + getFileExtension(Objects.requireNonNull(multipartFile.getOriginalFilename())));
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
/*
            if (!uploadPath.resolve(Objects.requireNonNull(oldName)).toFile().delete())
                throw new IOException("Could not update file: " + multipartFile.getOriginalFilename());
*/
            Path targetPath = uploadPath.resolve(Objects.requireNonNull(newName));
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException(ioe);
        }
        return newName;
    }

    public static void deleteFile(String dir, String fileName) throws IOException {
        Path deletePath = Paths
                .get("Files-Storage/" + dir);
        if (!Files.exists(deletePath)) {
            throw new IOException("file: " + fileName + " Not Exists");
        }
        try {
            if (!deletePath.resolve(Objects.requireNonNull(fileName)).toFile().delete())
                throw new IOException("Could not delete file: " + fileName);
        } catch (IOException ioe) {
            throw new IOException(ioe);
        }
    }


    public static String getFileExtension(String file) {
        var fileData = file.split("\\.");
        return fileData[fileData.length - 1];
    }

    public static void renameTo(String dir, String oldName, String name) throws IOException {
        Path uploadPath = Paths
                .get("Files-Storage/" + dir);

        if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        try {
            File a = uploadPath.resolve(Objects.requireNonNull(oldName)).toFile();
            boolean b = a.renameTo(uploadPath.resolve(Objects.requireNonNull(name)).toFile());
            if (b) {
                a.delete();
            }else {
                throw new IOException("Could not update file: " + name);
            }
        } catch (IOException ex) {
            throw new IOException("Could not update file: " + name);
        }
    }

    public static void moveFromTo(String dirOld,String dirNew, String oldName, String name) throws IOException {
        Path newPath = Paths
                .get("Files-Storage/" + dirNew);

        Path oldPath = Paths
                .get("Files-Storage/" + dirOld);

        if (!Files.exists(newPath)) {
            Files.createDirectories(newPath);
        }
        if (!Files.exists(oldPath)) {
            Files.createDirectories(oldPath);
        }

        File a = oldPath.resolve(Objects.requireNonNull(oldName)).toFile();
        boolean b = a.renameTo(newPath.resolve(Objects.requireNonNull(name)).toFile());
        if (b) {
           a.delete();
        }else {
            throw new IOException("Could not delete file: " + name);
        }
    }

    public static void copyFromTo(String dirOld,String dirNew, String oldName, String name) throws IOException {
        Path newPath = Paths
                .get("Files-Storage/" + dirNew);

        Path oldPath = Paths
                .get("Files-Storage/" + dirOld);

        if (!Files.exists(newPath)) {
            Files.createDirectories(newPath);
        }
        if(!Files.exists(oldPath)){
            Files.createDirectories(oldPath);
        }

        try {
            Files.copy(oldPath.resolve(Objects.requireNonNull(oldName)),
                    newPath.resolve(Objects.requireNonNull(name)),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IOException("Could not update file: " + name);
        }
    }
    public static boolean checkAllowedFile(Group group, MultipartFile file, RequestMethod method, String fileName,Long fileId) {
        if (group.getMax_files_count() != null && group.getMax_files_count() <
                (group.getFiles().size() + (method == RequestMethod.POST ? 1 : 0))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Group reach the maximum files count");
        }
        if (group.getMax_allowed_file_size_in_mb() != null && file != null && group.getMax_allowed_file_size_in_mb() <
                ((file.getSize() / (1024.0)) /1024.0) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The File size isn't allowed in this group max size is: "+ group.getMax_allowed_file_size_in_mb()+
                            "MB but your file is: "+ String.format("%.2f",(file.getSize() / (1024.0)) /1024.0) +"MB");
        }
        if (group.getFiles().stream().anyMatch(f -> f.getName().trim().equals(fileName.trim()) && f.getId() != fileId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The File Name Is already Exists.");
        }
        if (
                group.getAllowed_extension_file_types() != null
                        &&
                        !Arrays.stream(group.getAllowed_extension_file_types().trim().split("\\s*,\\s*"))
                                .toList().contains(
                                        getFileExtension(Objects.requireNonNull(file != null ? file.getOriginalFilename() : null))
                                )
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This File's Extension type is not allowed in this group");
        }
        return true;
    }
}
