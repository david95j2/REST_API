package com.example.restapi.file.pcd;

import com.example.restapi.configuration.FtpConfig;
import com.example.restapi.exception.AppException;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.file.pcd.domain.*;
import com.example.restapi.utils.Util;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.json.simple.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {
    private final MapRepository mapRepository;
    private final MapGroupRepository mapGroupRepository;
    private final MapGroupSampleRepository mapGroupSampleRepository;

    // 해당 유저의 한에 전체 pcd 리스트 불러오기 -> O
    public BaseResponse getPcdList(String login_id) {
        return new BaseResponse(ErrorCode.SUCCESS, mapGroupRepository.findAllByLoginId(login_id));
    }

    @Transactional(readOnly = true)
    public BaseResponse getGroupPcdList(String loginId, Integer mapGroupId) {
        List<MapEntity> map_results = mapRepository.findAllByMapGroupIdAndLoginId(mapGroupId,loginId);

        for (MapEntity entity : map_results) {
            File file = new File(entity.getMapPath());
            entity.setMapPath(file.getName());
        }

        return new BaseResponse(ErrorCode.SUCCESS, map_results);
    }

    @Transactional(readOnly = true)
    public ResponseEntity getPcdJson(Integer id, String login_id) {
        GetMapJsonMapping getMapJsonMapping = mapRepository.findDetailByIdAndLoginId(id, login_id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND));
        JSONObject result = new JSONObject();
        result.put("file_name",new File(getMapJsonMapping.getFileName()).getName());
        result.put("location",getMapJsonMapping.getLocation());
        result.put("count",getMapJsonMapping.getCount());
        result.put("area",getMapJsonMapping.getArea());
        result.put("regdate",getMapJsonMapping.getRegdate());


        return new ResponseEntity(
                new BaseResponse(ErrorCode.SUCCESS, result)
                ,ErrorCode.SUCCESS.getStatus());
    }

    public Resource getPcd(Integer id, String login_id) {
        GetMapInfoMapping getMapInfoMapping = mapRepository.findByIdAndLoginId(id, login_id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND)
        );

        return Util.loadFileAsResource(String.valueOf(Paths.get(getMapInfoMapping.getFileName()).getParent()),
                String.valueOf(Paths.get(getMapInfoMapping.getFileName()).getFileName()));
    }

    public Resource getPcdSample(Integer id, String login_id) {
        MapSampleMapping result = mapRepository.findSampleByIdAndLoginId(id, login_id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND));

        return Util.loadFileAsResource(Path.of(result.getPcdSamplePath()).getParent().toString().replace("\\","/"),
                Path.of(result.getPcdSamplePath()).getFileName().toString());
    }


}
