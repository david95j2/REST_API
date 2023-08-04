package com.example.restapi.webGCS;

import com.example.restapi.exception.BaseResponse;
import com.example.restapi.user.UserService;
import com.example.restapi.webGCS.domain.DeleteMissionReq;
import com.example.restapi.webGCS.domain.PostMissionReq;
import com.example.restapi.webGCS.domain.PostWayPointReq;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class WebGcsController {
    private final WebGcsService webGcsService;
    private final UserService userService;

    @GetMapping("/api/{login_id}/missions")
    @ResponseBody
    public BaseResponse getMissions(@PathVariable("login_id") String login_id) {
        userService.getUserByLoginId(login_id);
        return webGcsService.getMissionList(login_id);
    }

    @PostMapping("/api/{login_id}/mission")
    @ResponseBody
    public BaseResponse postMission(@PathVariable("login_id") String login_id,@Valid @RequestBody PostMissionReq postMissionReq) {
        Integer user_id = userService.getUserByLoginId(login_id);
        postMissionReq.setUser_id(user_id);
        return webGcsService.postMission(postMissionReq);
    }

    @DeleteMapping("/api/{login_id}/mission")
    @ResponseBody
    public BaseResponse deleteMission(@PathVariable("login_id") String login_id, @Valid @RequestBody DeleteMissionReq deleteMissionReq) {
        Integer user_id = userService.getUserByLoginId(login_id);
        deleteMissionReq.setUser_id(user_id);
        return webGcsService.deleteMission(deleteMissionReq);
    }

    @GetMapping("/api/{login_id}/mission/{mission_id}/waypoints")
    @ResponseBody
    public BaseResponse getWayPointList(@PathVariable("login_id") String login_id, @PathVariable("mission_id") Integer mission_id) {
        userService.getUserByLoginId(login_id);
        return webGcsService.getWayPointList(login_id, mission_id);
    }

    @PostMapping("/api/{login_id}/mission/{mission_id}/waypoint")
    @ResponseBody
    public BaseResponse postWayPoint(@PathVariable("login_id") String login_id, @PathVariable("mission_id") Integer mission_id,
                                     @Valid @RequestBody PostWayPointReq postWayPointReq) {
        userService.getUserByLoginId(login_id);
        return webGcsService.postWayPoint(mission_id, postWayPointReq);
    }
}
