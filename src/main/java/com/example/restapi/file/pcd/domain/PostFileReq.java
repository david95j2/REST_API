package com.example.restapi.file.pcd.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class PostFileReq {
    private String structure;
    private float latitude;
    private float longitude;
    private String regdate;
    private String location;
}
