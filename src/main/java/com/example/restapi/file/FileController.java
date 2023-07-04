package com.example.restapi.file;

import com.example.restapi.exception.BaseResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/api/pcds")
    @ResponseBody
    public BaseResponse getPcdList() {
        return fileService.getPcdList();
    }

    @GetMapping("/api/pcd/{id}/json")
    @ResponseBody
    public ResponseEntity getPcdJson(@PathVariable("id") int id) {return fileService.getPcdJson(id);}

    @GetMapping("/api/pcd/{id}")
    public ResponseEntity<InputStreamResource> getPcd(@PathVariable("id") int id) throws IOException {
        Resource file = fileService.getPcd(id);

        String fileName = file.getFilename();

        // Assume that the file extension is everything after the last dot
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

        String mediaType = getMediaTypeForExtension(fileExtension);
        if (mediaType == null) {
            throw new RuntimeException("Could not determine file type.");
        }

        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file.getFile()));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(mediaType))
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("Error while loading file " + fileName, e);
        }
    }


    @GetMapping("/api/pcd/{img_id}/images")
    @ResponseBody
    public BaseResponse getImageList(@PathVariable("img_id") int id) {
        return fileService.getImgList(id);
    }

    @GetMapping("/api/pcd/{pcd_id}/image/{img_id}/json")
    @ResponseBody
    public ResponseEntity getImageJson(@PathVariable("pcd_id") int id, @PathVariable("img_id") int img_id) {
        return fileService.getImgJson(id, img_id);
    }

    @GetMapping("/api/pcd/{pcd_id}/image/{img_id}")
    public ResponseEntity getImage(@PathVariable("pcd_id") int id,
                                                        @PathVariable("img_id") int img_id) throws IOException {
        Resource file = fileService.getImg(id, img_id);

        String fileName = file.getFilename();

        // Assume that the file extension is everything after the last dot
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

        String mediaType = getMediaTypeForExtension(fileExtension);
        if (mediaType == null) {
            throw new RuntimeException("Could not determine file type.");
        }

        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file.getFile()));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(mediaType))
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("Error while loading file " + fileName, e);
        }
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
}
