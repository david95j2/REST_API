package com.example.restapi.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
public class ServerConfig {
    @Value("${ftp.ip}")
    private String ftpIp;

    @Value("${ftp.port}")
    private String ftpPort;

    @Value("${ftp.id}")
    private String ftpId;

    @Value("${ftp.password}")
    private String ftpPassword;
}
