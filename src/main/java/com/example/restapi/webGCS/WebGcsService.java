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

@Service
@RequiredArgsConstructor
@Slf4j
public class WebGcsService {
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

    public BaseResponse deleteMission(Integer mission_id, Integer user_id) {
        MissionEntity missionEntity = missionRepository.findByIdAndUserEntityId(mission_id, user_id)
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_FOUND));
        // waypoint가 있으면 먼저 삭제
        Integer delete_waypoint_num = wayPointRepository.deleteALLByUserIdAndMissionId(mission_id, user_id);

        Integer delete_mission_id = missionRepository.deleteByIdAndUserEntityId(mission_id, user_id);
        if (delete_mission_id == 0) {
            return new BaseResponse(ErrorCode.DATA_NOT_FOUND);
        }

        return new BaseResponse(ErrorCode.SUCCESS, missionEntity.getMissionName()+" 데이터가 삭제되었습니다.");
    }

    public BaseResponse getWayPointList(Integer user_id, Integer missionId) {
        return new BaseResponse(ErrorCode.SUCCESS, wayPointRepository.findAllByMissionIdAndLoginId(missionId,user_id));
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

    public BaseResponse deleteWayPoint(Integer mission_id, Integer waypoint_id, Integer user_id) {
        /* mission_id 와 seq 를 조회해서 seq가 중간에 들어올 시 update해야 함.*/
        WayPointEntity wayPointEntity = wayPointRepository.findById(waypoint_id).orElseThrow(()->new AppException(ErrorCode.DATA_NOT_FOUND));
        wayPointRepository.decrementSeqGreaterThan(mission_id, wayPointEntity.getSeq());

        Integer delete_mission_id = wayPointRepository.deleteByIdAndUserIdAndMissionId(waypoint_id,mission_id,user_id);
        if (delete_mission_id == 0) {
            return new BaseResponse(ErrorCode.DATA_NOT_FOUND);
        }
        return new BaseResponse(ErrorCode.SUCCESS, String.valueOf(waypoint_id)+"번 Waypoint가 삭제되었습니다.");
    }
}
