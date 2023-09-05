package com.example.restapi.file.pcd;

import com.example.restapi.exception.AppException;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.file.pcd.domain.*;
import com.example.restapi.utils.Util;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapService {
    private final MapRepository mapRepository;
    private final MapGroupRepository mapGroupRepository;
    private final MapDateRepository mapDateRepository;
    private final MapGroupSampleRepository mapGroupSampleRepository;


    // 해당 유저의 한에 전체 pcd 리스트 불러오기 -> O
    public BaseResponse getPcdList(String login_id) {
        List<GetGroupInfoMapping> mapGroupInfos = mapGroupRepository.findMapGroupByUserId(login_id);
        return new BaseResponse(ErrorCode.SUCCESS, mapGroupInfos);
    }

    @Transactional(readOnly = true)
    public BaseResponse getGroupPcdList(Integer group_id) {
        List<MapDateEntity> results = mapDateRepository.findAllByMapGroupIdAndLoginId(group_id);
        List<JSONObject> result = results.stream()
                .map(x -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id",x.getId());
                    jsonObject.put("regdate",x.getDate()+" "+x.getTime());
                    return jsonObject;
                }).collect(Collectors.toList());

        return new BaseResponse(ErrorCode.SUCCESS, result);
    }

    public BaseResponse getPcdListByDate(Integer map_group_id, Integer map_date_id) {
        List<MapEntity> results = mapRepository.findAllByGroupIdAndDateId(map_group_id, map_date_id);

        List<GetPcdListRes> result = results.stream()
                .map(mapping -> {
                    String file_name = Paths.get(mapping.getMapPath()).getFileName().toString();
                    GetPcdListRes getPcdListRes = new GetPcdListRes();
                    getPcdListRes.setId(mapping.getId());
                    getPcdListRes.setFile_name(file_name);
                    return getPcdListRes;
                }).collect(Collectors.toList());
        return new BaseResponse(ErrorCode.SUCCESS, result);
    }

    public Resource getPcd(Integer id) {
        GetMapInfoMapping getMapInfoMapping = mapRepository.findByIdAndLoginId(id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND)
        );

        return Util.loadFileAsResource(String.valueOf(Paths.get(getMapInfoMapping.getFileName()).getParent()),
                String.valueOf(Paths.get(getMapInfoMapping.getFileName()).getFileName()));
    }

    public Resource getPcdSample(Integer id) {
        PageRequest pageRequest = PageRequest.of(0,1);
        MapSampleMapping result = mapGroupSampleRepository.findSampleById(id,pageRequest).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND));
        return Util.loadFileAsResource(Path.of(result.getFileName()).getParent().toString().replace("\\","/"),
                Path.of(result.getFileName()).getFileName().toString());
    }
}
