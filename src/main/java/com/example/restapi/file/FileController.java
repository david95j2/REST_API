package com.example.restapi.file;

import com.example.restapi.exception.BaseResponse;
import com.example.restapi.file.image.ImageService;
import com.example.restapi.file.pcd.MapService;
import com.example.restapi.user.UserService;
import com.example.restapi.utils.Util;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class FileController {
    private final MapService mapService;
    private final ImageService imageService;
    private final UserService userService;


    @GetMapping("api/{login_id}/pcds")
    @ResponseBody
    public BaseResponse getGroupList(@PathVariable("login_id") String login_id) {
        userService.getUserByLoginId(login_id);
        return mapService.getPcdList(login_id);
    }

    @GetMapping("api/pcds/{pcd_group_id}")
    @ResponseBody
    public BaseResponse getPcdDateList(@PathVariable("pcd_group_id") Integer map_group_id) {
        return mapService.getGroupPcdList(map_group_id);
    }

    @GetMapping("api/pcds/{pcd_group_id}/dates/{pcd_date_id}")
    @ResponseBody
    public BaseResponse getPcdList(@PathVariable("pcd_group_id") Integer map_group_id, @PathVariable("pcd_date_id") Integer map_date_id) {
        return mapService.getPcdListByDate(map_group_id, map_date_id);
    }


    @GetMapping("api/pcd/{pcd_id}")
    public ResponseEntity<InputStreamResource> getPcd(@PathVariable("pcd_id") Integer id) throws IOException {
        Resource file = mapService.getPcd(id);
        return Util.getFile(file, false);
    }

    @GetMapping("api/pcds/{pcd_group_id}/sample")
    public ResponseEntity<InputStreamResource> getPcdSample(@PathVariable("pcd_group_id") Integer id) throws IOException {
        Resource file = mapService.getPcdSample(id);
        return Util.getFile(file, true);
    }

    @GetMapping("api/pcds/{date_id}/images")
    @ResponseBody
    public BaseResponse getImageList(@PathVariable("date_id") Integer id) {
        return imageService.getImgList(id);
    }

    @GetMapping("api/images/{group_id}")
    @ResponseBody
    public BaseResponse getGroupImageList(@PathVariable("group_id") Integer group_id) {
        return imageService.getGroupImgList(group_id);
    }

    @GetMapping("api/image/{img_id}")
    public ResponseEntity getImage(@PathVariable("img_id") int img_id) throws IOException {
        Resource file = imageService.getImg(img_id);
        return Util.getFile(file, false);
    }

    @GetMapping("api/image/{img_id}/sample")
    public ResponseEntity getSampleImage(@PathVariable("img_id") int img_id) throws IOException {
        Resource file = imageService.getImg(img_id);
        return Util.getFile(file, true);
    }
}
