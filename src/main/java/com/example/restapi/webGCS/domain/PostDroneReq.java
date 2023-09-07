package com.example.restapi.webGCS.domain;

import com.example.restapi.user.domain.PostUserReq;
import com.example.restapi.user.domain.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDroneReq {
    @NotNull(message = "Filed Name이 drone_voltage_min 인지 확인하십시오.")
    private float drone_voltage_min;
    @NotNull(message = "Filed Name이 drone_voltage_max 인지 확인하십시오.")
    private float drone_voltage_max;
    @NotNull(message = "Filed Name이 drone_name 인지 확인하십시오.")
    private String drone_name;

}
