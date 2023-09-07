package com.example.restapi.webGCS;

import com.example.restapi.exception.AppException;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.user.UserRepository;
import com.example.restapi.user.domain.UserEntity;
import com.example.restapi.webGCS.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebGcsService {
    private final DroneRepository droneRepository;
    private final MissionRepository missionRepository;
    private final WayPointRepository wayPointRepository;
    private final UserRepository userRepository;
    public BaseResponse getMissionList(String loginId) {
        return new BaseResponse(ErrorCode.SUCCESS, missionRepository.findAllByLoginId(loginId));
    }

    public BaseResponse postMission(PostMissionReq postMissionReq) {
        UserEntity userEntity = userRepository.findById(postMissionReq.getUser_id()).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND)
        );
        MissionEntity missionEntity = MissionEntity.builder()
                        .missionName(postMissionReq.getMission_name())
                        .userEntity(userEntity).build();
        return new BaseResponse(ErrorCode.SUCCESS,missionRepository.save(missionEntity).getId());
    }

    public BaseResponse deleteMission(Integer mission_id) {
        MissionEntity missionEntity = missionRepository.findById(mission_id)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_FOUND));
        // waypoint가 있으면 먼저 삭제
        Integer delete_waypoint_num = wayPointRepository.deleteALLByMissionId(mission_id);

        missionRepository.delete(missionEntity);

        return new BaseResponse(ErrorCode.SUCCESS, missionEntity.getMissionName()+" 데이터가 삭제되었습니다.");
    }

    public BaseResponse getWayPointList(Integer mission_id) {
        return new BaseResponse(ErrorCode.SUCCESS, wayPointRepository.findAllByMissionId(mission_id));
    }

    public BaseResponse postWayPoint(Integer missionId, PostWayPointReq postWayPointReq) {
        MissionEntity missionEntity = missionRepository.findById(missionId).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND)
        );

        /* mission_id 와 seq 를 조회해서 seq가 중간에 들어올 시 update해야 함.*/
        wayPointRepository.incrementSeqGreaterThan(missionId,postWayPointReq.getSeq());

        WayPointEntity wayPointEntity = WayPointEntity.from(postWayPointReq, missionEntity);
        Integer id = wayPointRepository.save(wayPointEntity).getId();

        return new BaseResponse(ErrorCode.SUCCESS,id);
    }

    public BaseResponse deleteWayPoint(Integer waypoint_id) {
        /* mission_id 와 seq 를 조회해서 seq가 중간에 들어올 시 update해야 함.*/
        WayPointEntity wayPointEntity = wayPointRepository.findById(waypoint_id).orElseThrow(()->new AppException(ErrorCode.DATA_NOT_FOUND));
        wayPointRepository.decrementSeqGreaterThan(wayPointEntity.getMissionEntity().getId(), wayPointEntity.getSeq());

        wayPointRepository.delete(wayPointEntity);

        return new BaseResponse(ErrorCode.SUCCESS, String.valueOf(waypoint_id)+"번 Waypoint가 삭제되었습니다.");
    }

    public BaseResponse getDroneList(String loginId) {
        return new BaseResponse(ErrorCode.SUCCESS, droneRepository.findAllByLoginId(loginId));
    }

    public BaseResponse getDrone(Integer drone_id) {
        DroneEntity droneEntity = droneRepository.findById(drone_id).orElseThrow(
                ()-> new AppException(ErrorCode.DATA_NOT_FOUND)
        );

        return new BaseResponse(ErrorCode.SUCCESS, droneEntity);
    }

    public BaseResponse putDrone(PutDroneReq putDroneReq) {
        DroneEntity droneEntity = droneRepository.findById(putDroneReq.getDrone_id()).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        if (putDroneReq.getDrone_name() != null) {
            droneEntity.setDroneType(putDroneReq.getDrone_name());
        }
        if (putDroneReq.getDrone_voltage_min() != null) {
            droneEntity.setDroneVoltageMin(putDroneReq.getDrone_voltage_min());
        }
        if (putDroneReq.getDrone_voltage_max() != null) {
            droneEntity.setDroneVoltageMax(putDroneReq.getDrone_voltage_max());
        }
        droneRepository.save(droneEntity);

        return new BaseResponse(ErrorCode.SUCCESS, String.valueOf(putDroneReq.getDrone_id())+"번 Voltage값이 수정되었습니다.");
    }

    public BaseResponse postDrone(PostDroneReq postDroneReq, Integer user_id) {
        UserEntity userEntity = userRepository.findById(user_id).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND));

        DroneEntity droneEntity = DroneEntity.builder()
                .droneType(postDroneReq.getDrone_name())
                .droneVoltageMin(postDroneReq.getDrone_voltage_min())
                .droneVoltageMax(postDroneReq.getDrone_voltage_max())
                .userEntity(userEntity)
                .build();

        droneRepository.save(droneEntity).getId();
        return new BaseResponse(ErrorCode.SUCCESS, postDroneReq.getDrone_name()+" 드론이 추가되었습니다.");
    }
}
