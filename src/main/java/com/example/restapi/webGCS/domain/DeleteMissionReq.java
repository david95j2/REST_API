package com.example.restapi.webGCS.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteMissionReq {
    @NotNull(message = "Filed Name이 mission_id 인지 확인하십시오.")
    private Integer mission_id;
    private Integer user_id;
}
