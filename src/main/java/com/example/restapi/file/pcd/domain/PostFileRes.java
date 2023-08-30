package com.example.restapi.file.pcd.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter @Getter
public class PostFileRes {
    private String ftp_ip;
    private String ftp_port;
    private String ftp_id;
    private String ftp_password;
    private String folder_url;
}
