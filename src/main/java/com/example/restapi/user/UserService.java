package com.example.restapi.user;

import com.example.restapi.exception.AppException;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.user.domain.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    private String creatDate() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
    }
    private String createRestAPIKey() {
        return RandomStringUtils.randomAlphanumeric(15);
    }


    public BaseResponse getUser(int id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND));

        return new BaseResponse(ErrorCode.SUCCESS,
                new GetUserRes(userEntity.getId(),userEntity.getLoginId(),
                        userEntity.getPassword(), userEntity.getApiKey(), userEntity.getRegdate()));
    }

    public ResponseEntity postUser(PostUserReq postUserReq) {
        // 아이디 중복 체크
        userRepository.findByLoginId(postUserReq.getLogin_id()).ifPresent(
                userEntity -> {throw new AppException(ErrorCode.DUPLICATED_USER_NAME);}
        );

        postUserReq.setRegdate(creatDate());
        postUserReq.setApi_key(createRestAPIKey());
        UserEntity userEntity = postUserReq.toEntity(postUserReq);
        userRepository.save(userEntity);

        return new ResponseEntity(new BaseResponse(ErrorCode.CREATED, userEntity.toPostUserRes())
                , ErrorCode.CREATED.getStatus());
    }


    public ResponseEntity modifyUser(ModifyUserReq modifyUserReq) {
        // 아이디 체크
        UserEntity userEntity = userRepository.findByLoginId(modifyUserReq.getLogin_id()).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND));
        // 비밀번호 체크
        userRepository.findByPassword(modifyUserReq.getPassword())
                .orElseThrow(() -> new AppException(ErrorCode.INCORRECT));

        // 업데이트
        userEntity.updatePassword(modifyUserReq.getNew_passwd());

        return new ResponseEntity(
                new BaseResponse(ErrorCode.ACCEPTED,new GetUserRes(userEntity.getId(),userEntity.getLoginId(),
                        userEntity.getPassword(), userEntity.getApiKey(), userEntity.getRegdate()))
                ,ErrorCode.ACCEPTED.getStatus());
    }

    public ResponseEntity deleteUser(DeleteUserReq deleteUserReq) {
        // 비밀번호 체크
        userRepository.findByPassword(deleteUserReq.getPassword())
                .orElseThrow(() -> new AppException(ErrorCode.INCORRECT));
        UserEntity userEntity = userRepository.findByLoginIdAndPassword(deleteUserReq.getLogin_id(),deleteUserReq.getPassword())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        userRepository.deleteUser(userEntity.getId());

        return new ResponseEntity(
                new BaseResponse(ErrorCode.SUCCESS)
                , ErrorCode.SUCCESS.getStatus());
    }

    public BaseResponse login(LoginReq loginReq) {
        // 아이디 체크
        UserEntity userEntity = userRepository.findByLoginId(loginReq.getLogin_id()).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND  ));
        userRepository.findByPassword(loginReq.getPassword())
                .orElseThrow(() -> new AppException(ErrorCode.INCORRECT));

        return new BaseResponse(ErrorCode.SUCCESS,
                new GetUserRes(userEntity.getId(),userEntity.getLoginId(),
                        userEntity.getPassword(), userEntity.getApiKey(), userEntity.getRegdate()));
    }

    public BaseResponse getUserlist() {
        List list = userRepository.findAll();
        return new BaseResponse(ErrorCode.SUCCESS, list);
    }
}
