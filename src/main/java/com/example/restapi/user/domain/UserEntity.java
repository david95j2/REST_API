package com.example.restapi.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "login_id")
    private String loginId;
    private String password;
    private String regdate;
    @Column(name = "api_key")
    private String apiKey;
    @Column(name = "api_key_regdate")
    private String apiKeyRegdate;
    @Column(name = "api_key_status")
    private String apiKeyStatus;

    // 회원가입 (entity->DTO)
    public PostUserRes toPostUserRes() {
        return new PostUserRes(id);
    }
    public void updatePassword(String newPassword){
        this.password = newPassword;
    }
}
