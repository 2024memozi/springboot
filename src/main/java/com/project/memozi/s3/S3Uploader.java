package com.project.memozi.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid+"_"+originalFileName.replaceAll("\\s","_");

        String fileName = dirName + "/" +uniqueFileName;
        return putS3(multipartFile,fileName);


    }

    private String putS3(MultipartFile multipartFile, String fileName)throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()){
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
        } catch(IOException e){
            throw new IOException("S3 업로드 중 오류가 발생하였습니다",e);
        }
        return amazonS3Client.getUrl(bucket,fileName).toString();
    }

    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(bucket,fileName);
    }

    public String updateFile(MultipartFile newFile, String oldFileName, String dirName)throws IOException{
        deleteFile(oldFileName);
        return upload(newFile,dirName);
    }
}
