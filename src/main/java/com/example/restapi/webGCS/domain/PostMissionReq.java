package com.example.restapi.webGCS.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostMissionReq {
    @NotEmpty(message = "Filed Name이 mission_name 인지 확인하십시오.")
    private String mission_name;
    private Integer user_id;
}
