package com.example.restapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /*
     * 200 : 요청 성공
     */
    SUCCESS(HttpStatus.OK, "요청에 성공하였습니다."),
    /*
     * 201 : 생성 성공
     */
    CREATED(HttpStatus.CREATED,"생성되었습니다."),
    /*
     * 202 : 데이터 일부 or 전체 변경 성공
     */
    ACCEPTED(HttpStatus.ACCEPTED, "정보를 변경하였습니다."),

    /*
     * 400 : Request 오류
     */
    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, "URL을 다시 확인해주세요."),

    /*
     * 403 FORBIDDEN: 권한이 없는 사용자의 요청
     */
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "권한을 확인해주세요."),

    /*
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "데이터가 존재하지 않습니다."),
    INCORRECT(HttpStatus.NOT_FOUND, "비밀번호가 틀립니다."),
    NOT_NULL(HttpStatus.NOT_FOUND,"빈칸을 채워주십시오."),

    /*
     * 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 요청입니다. 보내실 데이터를 다시 한 번 확인해주세요."),

    /*
     * 409 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     */
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "이미 사용중입니다."),

    /*
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER ERROR");

    private final HttpStatus status;
    private final String message;
}
