package com.example.restapi.user.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginReq {
    @NotEmpty(message = "아이디는 필수 입력값입니다.")
    private String login_id;
    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}