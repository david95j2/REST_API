package com.example.restapi.file;

import com.example.restapi.configuration.FtpConfig;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.file.pcd.domain.PostFileReq;
import com.example.restapi.file.pcd.domain.PostFileRes;
import com.example.restapi.utils.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FtpService {
    private final FtpConfig ftpConfig;
    public BaseResponse getPcdURL(String login_id,String location, String date) {
        String url = StringUtils.joinWith("/",login_id,location,
                date.split("_")[0], date.split("_")[1]);

        return new BaseResponse(ErrorCode.SUCCESS, new PostFileRes(ftpConfig.getFtpIp(), ftpConfig.getFtpPort(),
                ftpConfig.getFtpId(),ftpConfig.getFtpPassword(),url));
    }

    public BaseResponse postPcdSample(PostFileReq postFileReq, String login_id) throws IOException {
        String url = StringUtils.joinWith("/",login_id,postFileReq.getLocation(),
                postFileReq.getRegdate().split("_")[0], postFileReq.getRegdate().split("_")[1]);
        FTPClient ftpClient = ftpConfig.open();
        try {
            List<String> results = Util.listFilesInDirectory(ftpClient,url);
            if (results.isEmpty()) {
                return new BaseResponse(ErrorCode.DATA_NOT_FOUND);
            }

            /* map_group post */
            /* map_group_sample post */
            /* map post */

            return new BaseResponse(ErrorCode.SUCCESS, results);
        } finally {
            ftpConfig.close(); // 항상 연결을 종료합니다.
        }
    }
}
