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

    @PostMapping("/ftp/{login_id}/pcd")
    @ResponseBody
    public BaseResponse postPcd(@PathVariable("login_id") String login_id, @Valid @RequestBody PostFileReq postFileReq) {
        Integer user_id = userService.getUserByLoginId(login_id);
        return mapService.getPcdURL(postFileReq,user_id,login_id);
    }
}
