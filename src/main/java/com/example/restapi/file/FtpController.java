package com.example.restapi.file;

import com.example.restapi.exception.BaseResponse;
import com.example.restapi.file.pcd.MapService;
import com.example.restapi.file.pcd.domain.PostFileReq;
import com.example.restapi.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@AllArgsConstructor
public class FtpController {
    private final FtpService ftpService;
    private final UserService userService;

    @GetMapping("ftp/{login_id}/{location}/{date}")
    @ResponseBody
    public BaseResponse getFtpFolderURL(@PathVariable("login_id") String login_id, @PathVariable("location") String location,
                                @PathVariable("date") String date) {
        userService.getUserByLoginId(login_id);
        return ftpService.getPcdURL(login_id,location,date);
    }

    @PostMapping("ftp/{login_id}/pcd/success")
    @ResponseBody
    public BaseResponse postPcd(@PathVariable("login_id") String login_id,
                                      @Valid @RequestBody PostFileReq postFileReq) throws IOException {
        return ftpService.postPcdSample(postFileReq, login_id);
    }
}
