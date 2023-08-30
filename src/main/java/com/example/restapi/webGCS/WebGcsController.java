package com.example.restapi.webGCS;

import com.example.restapi.exception.BaseResponse;
import com.example.restapi.user.UserService;
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

    @GetMapping("/api/{login_id}/drones")
    @ResponseBody
    public BaseResponse getDrones(@PathVariable("login_id") String login_id) {
        userService.getUserByLoginId(login_id);
        return webGcsService.getDroneList(login_id);
    }

    @GetMapping("/api/{login_id}/drone/{drone_id}")
    @ResponseBody
    public BaseResponse getDrone(@PathVariable("login_id") String login_id, @PathVariable("drone_id") Integer drone_id) {
        Integer user_id = userService.getUserByLoginId(login_id);
        return webGcsService.getDrone(user_id,drone_id);
    }

    @PutMapping("/api/{login_id}/drone/{drone_id}")
    @ResponseBody
    public BaseResponse putDrone(@PathVariable("login_id") String login_id, @PathVariable("drone_id") Integer drone_id,
                                 @Valid @RequestBody PutDroneReq putDroneReq) {
        Integer user_id = userService.getUserByLoginId(login_id);
        putDroneReq.setUser_id(user_id);
        putDroneReq.setDrone_id(drone_id);
        return webGcsService.putDrone(putDroneReq);
    }

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

    @DeleteMapping("/api/{login_id}/mission/{mission_id}")
    @ResponseBody
    public BaseResponse deleteMission(@PathVariable("login_id") String login_id, @PathVariable("mission_id") Integer mission_id) {
        Integer user_id = userService.getUserByLoginId(login_id);
        return webGcsService.deleteMission(mission_id, user_id);
    }

    @GetMapping("/api/{login_id}/mission/{mission_id}/waypoints")
    @ResponseBody
    public BaseResponse getWayPointList(@PathVariable("login_id") String login_id, @PathVariable("mission_id") Integer mission_id) {
        Integer user_id = userService.getUserByLoginId(login_id);
        return webGcsService.getWayPointList(user_id, mission_id);
    }

    @PostMapping("/api/{login_id}/mission/{mission_id}/waypoint")
    @ResponseBody
    public BaseResponse postWayPoint(@PathVariable("login_id") String login_id, @PathVariable("mission_id") Integer mission_id,
                                     @Valid @RequestBody PostWayPointReq postWayPointReq) {
        userService.getUserByLoginId(login_id);
        return webGcsService.postWayPoint(mission_id, postWayPointReq);
    }

    @DeleteMapping("/api/{login_id}/mission/{mission_id}/waypoint/{waypoint_id}")
    @ResponseBody
    public BaseResponse deleteWayPoint(@PathVariable("login_id") String login_id,@PathVariable("mission_id") Integer mission_id,
                                       @PathVariable("waypoint_id") Integer waypoint_id) {
        Integer user_id = userService.getUserByLoginId(login_id);
        return webGcsService.deleteWayPoint(mission_id,waypoint_id,user_id);
    }
}
