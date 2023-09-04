package com.example.restapi.file.image.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter @Setter
public class GetImagesRes {
    private Integer id;
    private String file_name;
    private String regdate;
    private Float pos_x;
    private Float pos_y;
    private Float pos_z;
    private Float roll;
    private Float pitch;
    private Float yaw;
}
