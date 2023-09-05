package com.example.restapi.file.image;

import com.example.restapi.exception.AppException;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.file.image.domain.*;
import com.example.restapi.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public BaseResponse getImgList(Integer id) {
        List<GetImageMapping> results = imageRepository.findAllByMapIdAndLoginId(id);
        return new BaseResponse(ErrorCode.SUCCESS, results);
    }

    @Transactional(readOnly = true)
    public BaseResponse getGroupImgList(Integer map_id,Integer group_id) {
        List<GetImagesMapping> results = imageRepository.findAllGroupByMapIdAndLoginId(map_id, group_id);

        List<GetImagesRes> result = results.stream()
                .map(mapping -> {
                    String fileName = Paths.get(mapping.getFileName()).getFileName().toString();
                    GetImagesRes getImagesRes = new GetImagesRes();
                    getImagesRes.setId(mapping.getId());
                    getImagesRes.setFile_name(fileName);
                    getImagesRes.setRegdate(mapping.getRegdate());
                    getImagesRes.setPos_x(mapping.getPosX());
                    getImagesRes.setPos_y(mapping.getPosY());
                    getImagesRes.setPos_z(mapping.getPosZ());
                    getImagesRes.setRoll(mapping.getRoll());
                    getImagesRes.setPitch(mapping.getPitch());
                    getImagesRes.setYaw(mapping.getYaw());
                    return getImagesRes;
                }).collect(Collectors.toList());

        return new BaseResponse(ErrorCode.SUCCESS, result);
    }


    public Resource getImg(Integer id, Integer img_id) {
        ImageEntity imageEntity = imageRepository.findPathByIdAndMapIdAndLoginId(img_id,id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND)
        );
        return Util.loadFileAsResource(Paths.get(imageEntity.getImgPath()).getParent().toString(),
                Paths.get(imageEntity.getImgPath()).getFileName().toString());
    }
}
