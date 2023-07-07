package com.example.restapi.file;

import com.example.restapi.exception.AppException;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.file.domain.GetFileRes;
import com.example.restapi.file.domain.ImageEntity;
import com.example.restapi.file.domain.PcdEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {
    private final PcdRepository pcdRepository;
    private final ImageRepository imageRepository;

    public BaseResponse getPcdList() {
        return new BaseResponse(ErrorCode.SUCCESS, pcdRepository.findAll());
    }

    public ResponseEntity getPcdJson(Integer id) {
        PcdEntity pcdEntity = pcdRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND)
        );

        return new ResponseEntity(
                new BaseResponse(ErrorCode.SUCCESS,new GetFileRes(pcdEntity.getPcd_name(),pcdEntity.getPcd_type(),
                        pcdEntity.getPcd_location(), pcdEntity.getPcd_regdate()))
                ,ErrorCode.SUCCESS.getStatus());
    }

    public Resource getPcd(Integer id) {
        PcdEntity pcdEntity = pcdRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND)
        );

        return loadFileAsResource(pcdEntity.getPcd_path(),pcdEntity.getPcd_name()+pcdEntity.getPcd_type());
    }

    public BaseResponse getImgList(int id) {
        return new BaseResponse(ErrorCode.SUCCESS, imageRepository.findAllByPcdEntityId(id));
    }

    public ResponseEntity getImgJson(int id, int img_id) {
        ImageEntity imageEntity = imageRepository.findByIdAndPcdEntityId(img_id, id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND)
        );

        return new ResponseEntity(
                new BaseResponse(ErrorCode.SUCCESS,imageEntity.toGetImageRes())
                ,ErrorCode.SUCCESS.getStatus());
    }

    public Resource getImg(int id, int img_id) {
        ImageEntity imageEntity = imageRepository.findByIdAndPcdEntityId(img_id,id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND)
        );

        return loadFileAsResource(imageEntity.getImg_path(),imageEntity.getImg_name()+imageEntity.getImg_type());
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
