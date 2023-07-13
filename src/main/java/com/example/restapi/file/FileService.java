package com.example.restapi.file;

import com.example.restapi.exception.AppException;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.file.domain.GetLoginIdReq;
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

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {
    private final PcdRepository pcdRepository;
    private final ImageRepository imageRepository;

    // 해당 유저의 한에 전체 pcd 리스트 불러오기 -> O
    public BaseResponse getPcdList(GetLoginIdReq getLoginIdReq) {
        return new BaseResponse(ErrorCode.SUCCESS,pcdRepository.findAllByUserEntity_LoginId(getLoginIdReq.getLogin_id()));
    }


    public ResponseEntity getPcdJson(int id, GetLoginIdReq getLoginIdReq) {
        PcdEntity pcdEntity = pcdRepository.findByIdAndUserEntityLoginId(id, getLoginIdReq.getLogin_id()).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND)
        );

        return new ResponseEntity(
                new BaseResponse(ErrorCode.SUCCESS,pcdEntity.toGetFileRes())
                ,ErrorCode.SUCCESS.getStatus());
    }

    public Resource getPcd(Integer id) {
        PcdEntity pcdEntity = pcdRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.DATA_NOT_FOUND)
        );

        return loadFileAsResource(pcdEntity.getPcdPath(),pcdEntity.getPcdName()+pcdEntity.getPcdType());
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
