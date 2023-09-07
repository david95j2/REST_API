package com.example.restapi.webGCS.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PutDroneReq {
    private Float drone_voltage_min;
    private Float drone_voltage_max;
    private String drone_name;
    private Integer drone_id;
}
