package com.example.restapi.webGCS;

import com.example.restapi.exception.AppException;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.user.UserRepository;
import com.example.restapi.user.domain.UserEntity;
import com.example.restapi.webGCS.domain.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
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

    public BaseResponse deleteMission(DeleteMissionReq deleteMissionReq) {
        MissionEntity missionEntity = missionRepository.findByIdAndUserEntityId(
                deleteMissionReq.getMission_id(),deleteMissionReq.getUser_id())
                .orElseThrow(()->new AppException(ErrorCode.DATA_NOT_FOUND));
        Integer mission_id = missionRepository.deleteByIdAndUserEntityId(
                deleteMissionReq.getMission_id(), deleteMissionReq.getUser_id());
        if (mission_id == 0) {
            return new BaseResponse(ErrorCode.DATA_NOT_FOUND);
        }

        return new BaseResponse(ErrorCode.SUCCESS, missionEntity.getMissionName()+" 데이터가 삭제되었습니다.");
    }

    public BaseResponse getWayPointList(String loginId, Integer missionId) {
        return new BaseResponse(ErrorCode.SUCCESS, wayPointRepository.findAllByMissionIdAndLoginId(missionId,loginId));
    }

    public BaseResponse postWayPoint(Integer missionId, PostWayPointReq postWayPointReq) {
        MissionEntity missionEntity = missionRepository.findById(missionId).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND)
        );
        WayPointEntity wayPointEntity = WayPointEntity.from(postWayPointReq, missionEntity);
        Integer id = wayPointRepository.save(wayPointEntity).getId();

        /* mission_id 와 seq 를 조회해서 seq가 중간에 들어올 시 update해야 함.*/
        wayPointRepository.incrementSeqGreaterThan(missionId,postWayPointReq.getSeq());

        return new BaseResponse(ErrorCode.SUCCESS,id);
    }
}
