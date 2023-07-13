package com.example.restapi.file.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter @Setter
public class GetImageRes {
    private String file_name;
    private String file_type;
    private String file_location;
    private String file_regdate;
    private float pos_x;
    private float pos_y;
    private float pos_z;
    private float roll;
    private float pitch;
    private float yaw;
}
