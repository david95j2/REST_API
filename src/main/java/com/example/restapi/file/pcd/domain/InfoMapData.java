package com.example.restapi.file.pcd.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InfoMapData {
    private String regdate;
    private String location;
    private Float latitude;
    private Float longitude;
    private Integer numberOfPointCloud;
    private Integer mapSize;
    private Float x;
    private Float y;
    private Float z;
}
