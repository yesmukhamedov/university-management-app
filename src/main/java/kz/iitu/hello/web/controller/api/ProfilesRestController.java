package kz.iitu.hello.web.controller.api;

import kz.iitu.hello.domain.entity.UserFile;
import kz.iitu.hello.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfilesRestController {
    private final FileStorageService fileStorageService;

    @PostMapping("/{userId}/avatar")
    public UserFile uploadAvatar(@PathVariable Long userId,
                                 @RequestParam("file") MultipartFile file) {
        return fileStorageService.saveAvatar(userId, file);
    }

    @PostMapping("/{userId}/documents")
    public List<UserFile> uploadDocuments(@PathVariable Long userId,
                                          @RequestParam("files") List<MultipartFile> files) {
        return fileStorageService.saveDocuments(userId, files);
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        UserFile userFile = fileStorageService.getFile(fileId);
        Resource resource = fileStorageService.loadFileAsResource(fileId);

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (userFile.getContentType() != null) {
            mediaType = MediaType.parseMediaType(userFile.getContentType());
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userFile.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/files/{fileId}")
    public void deleteFile(@PathVariable Long fileId) {
        fileStorageService.deleteFile(fileId);
    }
}
