package com.example.restapi.file.pcd;

import com.example.restapi.configuration.FtpConfig;
import com.example.restapi.exception.AppException;
import com.example.restapi.exception.BaseResponse;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.file.pcd.domain.*;
import com.example.restapi.user.UserRepository;
import com.example.restapi.user.UserService;
import com.example.restapi.user.domain.UserEntity;
import com.example.restapi.utils.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FtpService {
    private final FtpConfig ftpConfig;
    private final MapRepository mapRepository;
    private final MapGroupRepository mapGroupRepository;
    private final MapDateRepository mapDateRepository;
    private final MapGroupSampleRepository mapGroupSampleRepository;
    private final UserRepository userRepository;

    public BaseResponse getPcdURL(String login_id,String location, String date) {
        String url = StringUtils.joinWith("/",login_id,location,
                date.split("_")[0], date.split("_")[1]);

        return new BaseResponse(ErrorCode.SUCCESS, new PostFileRes(ftpConfig.getFtpIp(), ftpConfig.getFtpPort(),
                ftpConfig.getFtpId(),ftpConfig.getFtpPassword(),url));
    }

    public BaseResponse postPcdSample(PostFileReq postFileReq, String login_id, Integer user_id) throws IOException {
        /* update map_group location */
        MapGroupEntity mapGroupEntity = mapGroupRepository.findByLocation(postFileReq.getLocation()).orElse(null);
        if (mapGroupEntity != null) {
            mapGroupEntity.setLatitude(postFileReq.getLatitude());
            mapGroupEntity.setLongitude(postFileReq.getLongitude());
        }

        String url = StringUtils.joinWith("/",login_id,postFileReq.getLocation(),
                postFileReq.getRegdate().split("_")[0], postFileReq.getRegdate().split("_")[1]);

        FTPClient ftpClient = ftpConfig.open();
        try {
            List<String> results = Util.listFilesInDirectory(ftpClient,url+"/pcd");
            if (results.isEmpty()) {
                return new BaseResponse(ErrorCode.FTP_DATA_NOT_FOUND,url);
            }
            UserEntity userEntity = userRepository.findById(user_id).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
            /* infoMap.txt -> Dto */
            InfoMapData infoMapData = Util.extractInfoMapDataFromResults(results,ftpClient);

            /* map_group post */
            MapGroupEntity newMapGroupEntity = MapGroupEntity.builder()
                    .userEntity(userEntity)
                    .location(postFileReq.getLocation())
                    .latitude(postFileReq.getLatitude())
                    .longitude(postFileReq.getLongitude())
                    .build();
            mapGroupRepository.save(newMapGroupEntity).getId();

            /* map_group_date post */
            MapDateEntity mapDateEntity = MapDateEntity.builder()
                    .mapGroupEntity(newMapGroupEntity)
                    .date(postFileReq.getRegdate().split("_")[0])
                    .time(postFileReq.getRegdate().split("_")[1])
                    .build();
            mapDateRepository.save(mapDateEntity).getId();


            /* map_group_sample post */
            List<String> sampleFiles = results.stream()
                    .filter(path -> path.contains("pcd/sample/") && !path.endsWith("/"))
                    .collect(Collectors.toList());

            Integer map_group_sample_num = 0;
            for (String filePath : sampleFiles) {
                Path path = Paths.get(filePath);
//                System.out.println(new StringBuilder().append(path.getFileName().toString().split("_")[0]).append("_").append(path.getFileName().toString().split("_")[1].toString()));

                MapGroupSampleEntity mapGroupSampleEntity = MapGroupSampleEntity.builder()
                        .mapDateEntity(mapDateEntity)
                        .mapSamplePath("/hdd_ext/part6/sirius/"+filePath)
                        .mapSampleRegdate(Util.convertToMySQLFormat(path.getFileName().toString().split("_")[0]+"_"+path.getFileName().toString().split("_")[1]))
                        .build();
                mapGroupSampleRepository.save(mapGroupSampleEntity).getId();
                map_group_sample_num = map_group_sample_num + 1;
            }

            /* map post */
            List<String> pcdFiles = results.stream()
                    .filter(path -> path.endsWith(".pcd"))
                    .collect(Collectors.toList());
            Integer map_num = 0;

            for (String filePath : pcdFiles) {
                MapEntity mapEntity = MapEntity.builder()
                        .mapDateEntity(mapDateEntity)
                        .mapPath("/hdd_ext/part6/sirius/"+ filePath)
                        .mapCount(infoMapData.getNumberOfPointCloud())
                        .mapArea(infoMapData.getMapSize())
                        .build();
                mapRepository.save(mapEntity).getId();
                map_num = map_num + 1;
            }
            String message = "총 "+String.valueOf(map_group_sample_num+map_num)+"개의 파일이 업로드 되었습니다.";
            return new BaseResponse(ErrorCode.SUCCESS, message);
        } finally {
            ftpConfig.close(); // 항상 연결을 종료합니다.
        }
    }
}
