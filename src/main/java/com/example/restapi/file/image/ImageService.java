package com.example.restapi.file.image;

import com.example.restapi.exception.AppException;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.file.image.domain.GetImageMapping;
import com.example.restapi.file.image.domain.ImageEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
public class ImageService {
    private final ImageRepository imageRepository;

    public BaseResponse getImgList(Integer id, String login_id) {
        return new BaseResponse(ErrorCode.SUCCESS,
                imageRepository.findAllByMapIdAndLoginId(id, login_id));
    }

    public BaseResponse getGroupImgList(Integer map_id,Integer group_id, String login_id) {
        return new BaseResponse(ErrorCode.SUCCESS,
                imageRepository.findAllGroupByMapIdAndLoginId(map_id, group_id, login_id));
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

        return loadFileAsResource(imageEntity.getImgPath(),imageEntity.getImgName()+imageEntity.getImgType());
    }

    public Resource loadFileAsResource(String filePath, String fileName) {
        try {
            Path fileStorageLocation = Paths.get(filePath).toAbsolutePath().normalize();
            Path targetPath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(targetPath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }
}
