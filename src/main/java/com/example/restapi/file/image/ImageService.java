package com.example.restapi.file.image;

import com.example.restapi.exception.AppException;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.file.image.domain.GetGroupImagesMapping;
import com.example.restapi.file.image.domain.GetGroupImagesRes;
import com.example.restapi.file.image.domain.GetImageMapping;
import com.example.restapi.file.image.domain.ImageEntity;
import com.example.restapi.file.pcd.domain.MapEntity;
import com.example.restapi.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ImageService {
    private final ImageRepository imageRepository;

    public BaseResponse getImgList(Integer id, String login_id) {
        return new BaseResponse(ErrorCode.SUCCESS, imageRepository.findAllByMapIdAndLoginId(id, login_id));
    }

    @Transactional(readOnly = true)
    public BaseResponse getGroupImgList(Integer map_id,Integer group_id, String login_id) {
        List<GetGroupImagesMapping> getGroupImagesMappings = imageRepository.findAllGroupByMapIdAndLoginId(map_id, group_id, login_id);
        List<GetGroupImagesRes> result = getGroupImagesMappings.stream()
                .map(mapping -> {
                    String fileName = Paths.get(mapping.getImgPath()).getFileName().toString();
                    GetGroupImagesRes getGroupImagesRes = new GetGroupImagesRes();
                    getGroupImagesRes.setId(mapping.getId());
                    getGroupImagesRes.setImgName(fileName);
                    getGroupImagesRes.setRegdate(mapping.getRegdate());
                    return getGroupImagesRes;
                }).collect(Collectors.toList());

        return new BaseResponse(ErrorCode.SUCCESS, result);
    }

    public ResponseEntity getImgJson(Integer id, Integer img_id, String login_id) {
        GetImageMapping getImageMapping = imageRepository.findByIdAndMapIdAndLoginId(img_id, id, login_id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND));

        return new ResponseEntity(
                new BaseResponse(ErrorCode.SUCCESS,getImageMapping)
                ,ErrorCode.SUCCESS.getStatus());
    }

    public Resource getImg(Integer id, Integer img_id, String login_id) {
        ImageEntity imageEntity = imageRepository.findPathByIdAndMapIdAndLoginId(img_id,id,login_id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND)
        );
        return Util.loadFileAsResource(Paths.get(imageEntity.getImgPath()).getParent().toString(),
                Paths.get(imageEntity.getImgPath()).getFileName().toString());
    }
}
