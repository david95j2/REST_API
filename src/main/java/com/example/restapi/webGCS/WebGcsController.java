package com.example.restapi.webGCS;

import com.example.restapi.exception.BaseResponse;
import com.example.restapi.user.UserService;
import com.example.restapi.webGCS.domain.PostDroneReq;
import com.example.restapi.webGCS.domain.PostMissionReq;
import com.example.restapi.webGCS.domain.PostWayPointReq;
import com.example.restapi.webGCS.domain.PutDroneReq;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
@AllArgsConstructor
public class WebGcsController {
    private final WebGcsService webGcsService;
    private final UserService userService;

    @GetMapping("api/{login_id}/drones")
    @ResponseBody
    public BaseResponse getDrones(@PathVariable("login_id") String login_id) {
        userService.getUserByLoginId(login_id);
        return webGcsService.getDroneList(login_id);
    }

    @GetMapping("api/drone/{drone_id}")
    @ResponseBody
    public BaseResponse getDrone(@PathVariable("drone_id") Integer drone_id) {
        return webGcsService.getDrone(drone_id);
    }

    @PostMapping("api/{login_id}/drone")
    @ResponseBody
    public BaseResponse postDrone(@PathVariable("login_id") String login_id ,@Valid @RequestBody PostDroneReq postDroneReq) {
        Integer user_id = userService.getUserByLoginId(login_id);
        return webGcsService.postDrone(postDroneReq,user_id);
    }

    @PatchMapping("api/drone/{drone_id}")
    @ResponseBody
    public BaseResponse putDrone(@PathVariable("drone_id") Integer drone_id,@RequestBody PutDroneReq putDroneReq) {
        putDroneReq.setDrone_id(drone_id);
        return webGcsService.putDrone(putDroneReq);
    }

    @GetMapping("api/{login_id}/missions")
    @ResponseBody
    public BaseResponse getMissions(@PathVariable("login_id") String login_id) {
        userService.getUserByLoginId(login_id);
        return webGcsService.getMissionList(login_id);
    }

    @PostMapping("api/{login_id}/mission")
    @ResponseBody
    public BaseResponse postMission(@PathVariable("login_id") String login_id,@Valid @RequestBody PostMissionReq postMissionReq) {
        Integer user_id = userService.getUserByLoginId(login_id);
        postMissionReq.setUser_id(user_id);
        return webGcsService.postMission(postMissionReq);
    }

    @DeleteMapping("api/mission/{mission_id}")
    @ResponseBody
    public BaseResponse deleteMission(@PathVariable("mission_id") Integer mission_id) {
        return webGcsService.deleteMission(mission_id);
    }

    @GetMapping("api/mission/{mission_id}/waypoints")
    @ResponseBody
    public BaseResponse getWayPointList(@PathVariable("mission_id") Integer mission_id) {
        return webGcsService.getWayPointList(mission_id);
    }

    @PostMapping("api/mission/{mission_id}/waypoint")
    @ResponseBody
    public BaseResponse postWayPoint(@PathVariable("mission_id") Integer mission_id, @Valid @RequestBody PostWayPointReq postWayPointReq) {
        return webGcsService.postWayPoint(mission_id, postWayPointReq);
    }

    @DeleteMapping("api/mission/waypoint/{waypoint_id}")
    @ResponseBody
    public BaseResponse deleteWayPoint(@PathVariable("waypoint_id") Integer waypoint_id) {
        return webGcsService.deleteWayPoint(waypoint_id);
    }
}
