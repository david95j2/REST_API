package com.example.restapi.file.pcd;

import com.example.restapi.configuration.ServerConfig;
import com.example.restapi.exception.AppException;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.file.pcd.domain.MapEntity;
import com.example.restapi.file.pcd.domain.MapSampleMapping;
import com.example.restapi.file.pcd.domain.PostFileReq;
import com.example.restapi.file.pcd.domain.PostFileRes;
import com.example.restapi.utils.Util;
import io.swagger.models.auth.In;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Transactional
public class MapService {
    private final MapRepository mapRepository;
    private final ServerConfig serverConfig;

    // 해당 유저의 한에 전체 pcd 리스트 불러오기 -> O
    public BaseResponse getPcdList(String login_id) {
        return new BaseResponse(ErrorCode.SUCCESS, mapRepository.findAllByUserEntity_LoginId(login_id));
    }

    public ResponseEntity getPcdJson(Integer id, String login_id) {
        MapEntity mapEntity = mapRepository.findByIdAndLoginId(id, login_id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND));

        return new ResponseEntity(
                new BaseResponse(ErrorCode.SUCCESS, mapEntity.toGetFileRes())
                ,ErrorCode.SUCCESS.getStatus());
    }

    public Resource getPcd(Integer id, String login_id) {
        MapEntity mapEntity = mapRepository.findByIdAndLoginId(id, login_id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND)
        );
        return Util.loadFileAsResource(mapEntity.getMapPath(), mapEntity.getMapName()+ mapEntity.getMapType());
    }

    public Resource getPcdSample(Integer id, String login_id) {
        MapSampleMapping result = mapRepository.findSampleByIdAndLoginId(id, login_id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND));

        return Util.loadFileAsResource(Path.of(result.getPcdSamplePath()).getParent().toString().replace("\\","/"),
                Path.of(result.getPcdSamplePath()).getFileName().toString());
    }

    public BaseResponse getPcdURL(PostFileReq postFileReq, Integer userId, String login_id) {
        String url = StringUtils.joinWith("/",login_id,postFileReq.getLocation(),
                postFileReq.getRegdate().split(" ")[0], postFileReq.getRegdate().split(" ")[1]);

        return new BaseResponse(ErrorCode.SUCCESS, new PostFileRes(serverConfig.getFtpId(),serverConfig.getFtpIp(),url,
                serverConfig.getFtpPassword(),serverConfig.getFtpPort()));
    }
}
