package com.example.restapi.configuration;

import com.example.restapi.utils.Util;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@Getter
@Slf4j
public class FtpConfig {
    @Value("${ftp.ip}")
    private String ftpIp;

    @Value("${ftp.port}")
    private Integer ftpPort;

    @Value("${ftp.id}")
    private String ftpId;

    @Value("${ftp.password}")
    private String ftpPassword;
    private String ftpURL;
    private FTPClient ftpClient;

    public FTPClient open() {
        ftpClient = new FTPClient();
        //default controlEncoding 값이 "ISO-8859-1" 때문에 한글 파일의 경우 파일명이 깨짐
        //ftp server 에 저장될 파일명을 uuid 등의 방식으로 한글을 사용하지 않고 저장할 경우 UTF-8 설정이 따로 필요하지 않다.
//        ftpClient.setControlEncoding("UTF-8");
        //PrintCommandListener 를 추가하여 표준 출력에 대한 명령줄 도구를 사용하여 FTP 서버에 연결할 때 일반적으로 표시되는 응답을 출력
//        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

        try {
            //ftp 서버 연결
            ftpClient.connect(ftpIp, ftpPort);
            // 로그인 이전에 Passive 모드로 전환
            ftpClient.enterLocalPassiveMode();
            //ftp 서버에 정상적으로 연결되었는지 확인
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                log.error("FTPClient:: server connection failed.");
                return null;
            }

            //socketTimeout 값 설정
            ftpClient.setSoTimeout(1000);
            //ftp 서버 로그인
            ftpClient.login(ftpId, ftpPassword);
            //file type 설정 (default FTP.ASCII_FILE_TYPE)
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        } catch (IOException e) {
            e.printStackTrace();
            log.error("FTPClient:: server connection failed.");
            return null;
        }
        return ftpClient;
    }

    public void close() {
        try {
            if(ftpClient != null && ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            log.error("FTPClient:: server close failed.", e);
        }
    }
}
