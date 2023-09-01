package com.example.restapi.file.image.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter @Setter
public class GetImageRes {
    private String file_location;
    private String file_regdate;
    private Float pos_x;
    private Float pos_y;
    private Float pos_z;
    private Float roll;
    private Float pitch;
    private Float yaw;
}
