package com.project.memozi.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final String baseDir = "uploads/";

    public String upload(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        String fileName = baseDir+uniqueFileName;
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

    public void deleteFile(String filePath) throws IOException{
        try {
            System.out.println("삭제할 파일 경로: " + filePath);
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, filePath));
            System.out.println("파일을 삭제하였습니다: " + filePath);
        } catch (AmazonServiceException e) {
            System.err.println("AmazonServiceException: " + e.getMessage());
            throw e;
        } catch (SdkClientException e) {
            System.err.println("SdkClientException: " + e.getMessage());
            throw e;
        }
    }

    public String updateFile(MultipartFile newFile, String oldFileName)throws IOException{
        deleteFile(oldFileName);
        return upload(newFile);
    }
}
