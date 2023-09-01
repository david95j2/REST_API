package com.example.restapi.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private int id;
    private String login_id;
    private String password;
    private String regdate;

}
