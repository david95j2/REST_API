package com.example.restapi.file.pcd;

import com.example.restapi.configuration.FtpConfig;
import com.example.restapi.exception.AppException;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.file.image.domain.GetGroupImagesRes;
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
import java.io.FilterOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapService {
    private final MapRepository mapRepository;
    private final MapGroupRepository mapGroupRepository;
    private final MapGroupSampleRepository mapGroupSampleRepository;

    // 해당 유저의 한에 전체 pcd 리스트 불러오기 -> O
    public BaseResponse getPcdList(String login_id) {
        List<GetGroupInfoMapping> mapGroupInfos = mapGroupRepository.findMapGroupInfoByUserId(login_id);
        List<Map<String, Object>> responseList = new ArrayList<>();

        Map<String, List<GetGroupInfoMapping>> groupByLocation = mapGroupInfos.stream()
                .collect(Collectors.groupingBy(GetGroupInfoMapping::getLocation));

        groupByLocation.forEach((location, infos) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("location", location);

            if (infos.size() == 1) {
                GetGroupInfoMapping info = infos.get(0);
                map.put("id",info.getMapGroupId());
                map.put("latitude", info.getLatitude());
                map.put("longitude", info.getLongitude());
                map.put("regdate", info.getRegdate().toString());
            } else {
                List<Map<String, Object>> dataCounts = new ArrayList<>();
                for (GetGroupInfoMapping info : infos) {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("id",info.getMapGroupId());
                    dataMap.put("latitude", info.getLatitude());
                    dataMap.put("longitude", info.getLongitude());
                    dataMap.put("regdate", info.getRegdate().toString());
                    dataCounts.add(dataMap);
                }
                map.put("data_count", dataCounts);
            }

            responseList.add(map);
        });

        return new BaseResponse(ErrorCode.SUCCESS, responseList);
    }

    @Transactional(readOnly = true)
    public BaseResponse getGroupPcdList(String loginId, Integer mapGroupId) {
        List<GetGroupListMapping> map_results = mapRepository.findAllByMapGroupIdAndLoginId(mapGroupId,loginId);
        List<GetGroupPcdRes> result = map_results.stream()
                .map(mapping -> {
                    String fileName = Paths.get(mapping.getFileName()).getFileName().toString();
                    GetGroupPcdRes getGroupPcdRes = new GetGroupPcdRes();
                    getGroupPcdRes.setId(mapping.getPcdId());
                    getGroupPcdRes.setFile_name(fileName);
                    getGroupPcdRes.setRegdate(mapping.getRegdate());
                    return getGroupPcdRes;
                }).collect(Collectors.toList());
        return new BaseResponse(ErrorCode.SUCCESS, result);
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
