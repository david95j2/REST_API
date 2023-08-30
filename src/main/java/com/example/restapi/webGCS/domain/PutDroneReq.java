package com.example.restapi.webGCS.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PutDroneReq {
    @NotNull(message = "Filed Name이 drone_voltage_min 인지 확인하십시오.")
    private float drone_voltage_min;
    @NotNull(message = "Filed Name이 drone_voltage_max 인지 확인하십시오.")
    private float drone_voltage_max;
    private Integer user_id;
    private Integer drone_id;
}
