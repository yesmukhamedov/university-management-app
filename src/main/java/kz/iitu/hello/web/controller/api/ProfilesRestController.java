package kz.iitu.hello.web.controller.api;

import kz.iitu.hello.domain.entity.User;
import kz.iitu.hello.domain.entity.UserFile;
import kz.iitu.hello.domain.enums.UserRole;
import kz.iitu.hello.service.FileStorageService;
import kz.iitu.hello.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfilesRestController {
    private final FileStorageService fileStorageService;
    private final UserService userService;

    @PostMapping("/{userId}/avatar")
    public UserFile uploadAvatar(@PathVariable Long userId,
                                 @RequestParam("file") MultipartFile file,
                                 Authentication authentication) {
        verifyOwnerOrAdmin(userId, authentication);
        return fileStorageService.saveAvatar(userId, file);
    }

    @PostMapping("/{userId}/documents")
    public List<UserFile> uploadDocuments(@PathVariable Long userId,
                                          @RequestParam("files") List<MultipartFile> files,
                                          Authentication authentication) {
        verifyOwnerOrAdmin(userId, authentication);
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
    public void deleteFile(@PathVariable Long fileId, Authentication authentication) {
        Long userId = fileStorageService.getFile(fileId).getUser().getId();
        verifyOwnerOrAdmin(userId, authentication);
        fileStorageService.deleteFile(fileId);
    }

    private void verifyOwnerOrAdmin(Long userId, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        if (!currentUser.getId().equals(userId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: you can only modify your own profile");
        }
    }
}
