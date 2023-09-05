package com.example.restapi.utils;

import com.example.restapi.configuration.FtpConfig;
import com.example.restapi.file.pcd.domain.InfoMapData;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Util {

    public static String convertToMySQLFormat(String input) {
        String datePart = input.substring(0, 8); // 20230829
        String timePart = input.substring(9);     // 161503

        String year = datePart.substring(0, 4);
        String month = datePart.substring(4, 6);
        String day = datePart.substring(6, 8);

        String hour = timePart.substring(0, 2);
        String minute = timePart.substring(2, 4);
        String second = timePart.substring(4, 6);

        return String.format("%s-%s-%s %s:%s:%s", year, month, day, hour, minute, second);
    }
    public static String convertFromMySQLFormat(String input) {
        String datePart = input.split(" ")[0];
        String timePart = input.split(" ")[1];

        String year = datePart.split("-")[0];
        String month = datePart.split("-")[1];
        String day = datePart.split("-")[2];

        String hour = timePart.split(":")[0];
        String minute = timePart.split(":")[1];
        String second = timePart.split(":")[2];

        return year + month + day + "_" + hour + minute + second;
    }

    private static String getMediaTypeForExtension(String extension) {
        switch (extension.toLowerCase()) {
            case "jpeg":
            case "jpg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "pcd":
                return "application/octet-stream";
            default:
                return null;
        }
    }

    public static ResponseEntity getFile(Resource file, Boolean isSample) throws IOException {

        String fileName = file.getFilename();
//        System.out.println((file.contentLength() / 1024) / 1024 );
        // Assume that the file extension is everything after the last dot
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

        String mediaType = getMediaTypeForExtension(fileExtension);
        if (mediaType == null) {
            throw new RuntimeException("Could not determine file type.");
        }

        try {
            FileSystemResource resource;
            if (isSample) {
                // Create a temporary file to store the thumbnail
                File thumbnail = File.createTempFile("thumbnail", "." + fileExtension);
                try (InputStream in = new FileInputStream(file.getFile())) {
                    // Use Thumbnailator to create the thumbnail
                    Thumbnails.of(in)
                            .size(320, 200)  // Set the dimensions of the thumbnail. Adjust as needed.
                            .toFile(thumbnail);
                }
                resource = new FileSystemResource(thumbnail);
            } else {
                resource = new FileSystemResource(file.getFile());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()));
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(mediaType))
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("Error while loading file " + fileName, e);
        }
    }

    public static Resource loadFileAsResource(String filePath, String fileName) {
        try {
            Path fileStorageLocation = Paths.get(filePath).toAbsolutePath().normalize();
            Path targetPath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(targetPath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " +filePath+" "+ fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("[MalformedURLException Error] File not found " + fileName, ex);
        }
    }

    public static List<String> listFilesInDirectory(FTPClient ftpClient, String directory) throws IOException {
        List<String> fileList = new ArrayList<>();
        retrieveFiles(directory, fileList, ftpClient);
        return fileList;
    }

    private static void retrieveFiles(String directory, List<String> fileList, FTPClient ftpClient) throws IOException {
        FTPFile[] files = ftpClient.listFiles(directory);
        for (FTPFile file : files) {
            if (file.isFile()) {
                fileList.add(directory + "/" + file.getName());
            } else if (file.isDirectory()) {
                retrieveFiles(directory + "/" + file.getName(), fileList, ftpClient);
            }
        }
    }

    public static InfoMapData extractInfoMapDataFromResults(List<String> results, FTPClient ftpClient) throws IOException {
        String infoMapFilePath = results.stream()
                .filter(path -> path.endsWith("infoMap.txt"))
                .findFirst()
                .orElse(null);

        if (infoMapFilePath == null) {
            throw new FileNotFoundException("infoMap.txt not found in the provided list.");
        }

        return parseInfoMapFromFtp(ftpClient, infoMapFilePath);
    }

    private static InfoMapData parseInfoMapFromFtp(FTPClient ftpClient, String filePath) throws IOException {
        try (InputStream is = ftpClient.retrieveFileStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            InfoMapData infoMapData = new InfoMapData();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" : ");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    switch (key) {
                        case "regdate":
                            infoMapData.setRegdate(value);
                            break;
                        case "location":
                            infoMapData.setLocation(value);
                            break;
                        case "latitude":
                            infoMapData.setLatitude(Float.parseFloat(value));
                            break;
                        case "longitude":
                            infoMapData.setLongitude(Float.parseFloat(value));
                            break;
                        case "number of pointcloud":
                            infoMapData.setNumberOfPointCloud(Integer.parseInt(value));
                            break;
                        case "map size":
                            infoMapData.setMapSize(Integer.parseInt(value));
                            break;
                        case "x":
                            infoMapData.setX(Float.parseFloat(value));
                            break;
                        case "y":
                            infoMapData.setY(Float.parseFloat(value));
                            break;
                        case "z":
                            infoMapData.setZ(Float.parseFloat(value));
                            break;
                    }
                }
            }

            return infoMapData;
        }
    }
}
