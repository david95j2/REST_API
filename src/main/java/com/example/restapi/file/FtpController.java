package com.example.restapi.file;

import com.example.restapi.exception.BaseResponse;
import com.example.restapi.file.pcd.MapService;
import com.example.restapi.file.pcd.domain.PostFileReq;
import com.example.restapi.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class FtpController {
    private final MapService mapService;
    private final UserService userService;

    @GetMapping("ftp/{login_id}/{location}/{date}")
    @ResponseBody
    public BaseResponse getFtpFolderURL(@PathVariable("login_id") String login_id, @PathVariable("location") String location,
                                @PathVariable("date") String date) {
        userService.getUserByLoginId(login_id);
        return mapService.getPcdURL(login_id,location,date);
    }

//    @PostMapping("ftp/{login_id}/pcd/sample/success")
//    @ResponseBody
//    public BaseResponse postPcdSample(@PathVariable("login_id") String login_id,
//                                      @Valid @RequestBody PostFileReq postFileReq) {
//        return mapService.postPcdSample(postFileReq, login_id);
//    }
}
