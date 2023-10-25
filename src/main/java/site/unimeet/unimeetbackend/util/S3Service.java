package site.unimeet.unimeetbackend.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.file.FileIOException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    private final S3Client s3Client;

    // return fullFilePath
    public List<String> upload(List<MultipartFile> multipartFiles, String bucketNameSuffix){
        List<String> storedFilePaths = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            // empty Check. type=file 이며 name이 일치한다면, 본문이 비어있어도 MultiPartFile 객체가 생성된다.
            // 위의 이유로
            String storedFileName = upload(multipartFile, bucketNameSuffix);
            if (storedFileName == null){
                continue;
            }
            storedFilePaths.add(storedFileName);
        }
        // 저장한 파일의 경로 리스트를 반환한다.
        return storedFilePaths;
    }
    // ex: bucketNameSuffix == "/profile"
        // bucketName/profile/fileName.ext 로 저장됨
    public String upload(MultipartFile multipartFile, String bucketNameSuffix){
        // type=file 이며 name이 일치한다면, 본문이 비어있어도 MultiPartFile 객체가 생성된다.
        // 그래서 empty Check 하면 null 반환
        if (multipartFile.isEmpty()){
            return null;
        }
        String originalFileName = multipartFile.getOriginalFilename();
        String storedFileName = createStoredFileName(originalFileName);
        String storedFilePath = createStoredFilePath(storedFileName, bucketNameSuffix); // upload 메서드의 반환값
        // dir  + / + filenanme

        try (InputStream inputStream = multipartFile.getInputStream()){
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .contentType(multipartFile.getContentType())
                    .bucket(bucketName)
                    .key(bucketNameSuffix + "/" + storedFileName)
                    .build();

            log.error("bucketName : {}, bucketNameSuffix : {}, storedFileName : {}", bucketName, bucketNameSuffix, storedFileName);

            RequestBody requestBody = RequestBody.fromInputStream(inputStream, multipartFile.getSize());
            log.error("contentType = {} ", requestBody.contentType());
            log.error("contentLength = {}", requestBody.optionalContentLength().orElse(0L));
            // Upload the file to the specified bucket
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (IOException e) {
            // FileIOException 발생시키기 전에, IOEXCEPTION 에 대한 로그를 남긴다.
            log.error("IOEXCEPTION: " + "originalFileName: " + originalFileName +
                    ", storedFilePath: "+ storedFilePath + " 저장 불가" );
            e.printStackTrace();
            throw new FileIOException(ErrorCode.FILE_CANNOT_BE_STORED);
        }

        return storedFilePath;
    }

    private String createStoredFilePath(String storedFileName, String bucketSuffix) {
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com" + "/" + bucketSuffix + "/" + storedFileName;
    }

    private String createStoredFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFileName);

        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos +1);
    }


}
