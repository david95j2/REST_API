package com.example.restapi.file;

import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.file.domain.GetLoginIdReq;
import com.example.restapi.user.UserService;
import jakarta.validation.Valid;
import jep.Jep;
import jep.JepConfig;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping("/api/jep_test")

    @ResponseBody
    public String jepTest() {
        fileService.JepTest();
        return "Success !";
    }

    @GetMapping("/api/{login_id}/pcds")
    @ResponseBody
    public BaseResponse getPcdList(@PathVariable String login_id) {
        userService.getUserByLoginId(login_id);
        return fileService.getPcdList(login_id);
    }

    @GetMapping("/api/{login_id}/pcd/{id}/info")
    @ResponseBody
    public ResponseEntity getPcdJson(
            @PathVariable("login_id") String login_id,@PathVariable("id") Integer id) {
        userService.getUserByLoginId(login_id);
        return fileService.getPcdJson(id, login_id);
    }


    @GetMapping("/api/{login_id}/pcd/{id}")
    public ResponseEntity<InputStreamResource> getPcd(
            @PathVariable("login_id") String login_id,@PathVariable("id") Integer id) throws IOException {
        userService.getUserByLoginId(login_id);
        Resource file = fileService.getPcd(id, login_id);
        return getFile(file, false);
    }

    @GetMapping("/api/{login_id}/pcd/{id}/sample")
    public ResponseEntity<InputStreamResource> getPcdSample(
            @PathVariable("login_id") String login_id,@PathVariable("id") Integer id) throws IOException {
        userService.getUserByLoginId(login_id);
        Resource file = fileService.getPcdSample(id, login_id);
        return getFile(file, true);
    }

    @GetMapping("/api/{login_id}/pcd/{id}/images")
    @ResponseBody
    public BaseResponse getImageList(@PathVariable("login_id") String login_id,@PathVariable("id") Integer id) {
        userService.getUserByLoginId(login_id);
        return fileService.getImgList(id, login_id);
    }

    @GetMapping("/api/{login_id}/pcd/{id}/images/{group_id}")
    @ResponseBody
    public BaseResponse getGroupImageList(
            @PathVariable("login_id") String login_id,@PathVariable("id") Integer id,
            @PathVariable("group_id") Integer group_id) {
        userService.getUserByLoginId(login_id);
        return fileService.getGroupImgList(id, group_id,login_id);
    }

    @GetMapping("/api/{login_id}/pcd/{pcd_id}/image/{img_id}/info")
    @ResponseBody
    public ResponseEntity getImageJson(@PathVariable("login_id") String login_id,
            @PathVariable("pcd_id") Integer pcd_id, @PathVariable("img_id") Integer img_id) {
        userService.getUserByLoginId(login_id);
        return fileService.getImgJson(pcd_id, img_id, login_id);
    }

    @GetMapping("/api/{login_id}/pcd/{pcd_id}/image/{img_id}")
    public ResponseEntity getImage(@PathVariable("login_id") String login_id,
                                    @PathVariable("pcd_id") int id,
                                   @PathVariable("img_id") int img_id) throws IOException {
        Resource file = fileService.getImg(id, img_id, login_id);
        return getFile(file, false);
    }

    @GetMapping("/api/{login_id}/pcd/{pcd_id}/image/{img_id}/sample")
    public ResponseEntity getSampleImage(@PathVariable("login_id") String login_id,
                                   @PathVariable("pcd_id") int id,
                                   @PathVariable("img_id") int img_id) throws IOException {
        Resource file = fileService.getImg(id, img_id, login_id);

        return getFile(file, true);
    }

    private String getMediaTypeForExtension(String extension) {
        switch (extension.toLowerCase()) {
            case "jpeg":
            case "jpg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "pcd":
                return "application/octet-stream";
            default:
                return null;
        }
    }

    private ResponseEntity getFile(Resource file, Boolean isSample) throws IOException {

        String fileName = file.getFilename();
//        System.out.println((file.contentLength() / 1024) / 1024 );
        // Assume that the file extension is everything after the last dot
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

        String mediaType = getMediaTypeForExtension(fileExtension);
        if (mediaType == null) {
            throw new RuntimeException("Could not determine file type.");
        }

        try {
            FileSystemResource resource;
            if (isSample) {
                // Create a temporary file to store the thumbnail
                File thumbnail = File.createTempFile("thumbnail", "." + fileExtension);
                try (InputStream in = new FileInputStream(file.getFile())) {
                    // Use Thumbnailator to create the thumbnail
                    Thumbnails.of(in)
                            .size(320, 200)  // Set the dimensions of the thumbnail. Adjust as needed.
                            .toFile(thumbnail);
                }
                resource = new FileSystemResource(thumbnail);
            } else {
                resource = new FileSystemResource(file.getFile());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()));
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(mediaType))
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("Error while loading file " + fileName, e);
        }
    }
}
