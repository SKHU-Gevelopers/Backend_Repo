package site.unimeet.unimeetbackend.global.config.cloud;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    // 외부 클래스에서 객체 주입 없이 S3Config.defaultProfileImageUrl()를 통해 기본프사를 가져올 수 있도록 static으로 선언, 불변 private
    // 근데 이거 하겠다고 생성자랑 defaultProfileImageUrl() 추가해서 오히려 코드가 복잡해진 느낌???
    public static final String DEFAULT_PROFILE_IMAGE_URL = "https://unimeet-bucket.s3.ap-northeast-2.amazonaws.com/user_profile_img/ae5e08b9-9b37-433f-a2a5-03d720bd853e.png";
    public static final String BUCKETNAME_SUFFIX_PROFILE_IMG = "user_profile_img";
    public static final String BUCKETNAME_SUFFIX_POST_IMG = "post_img";
    public static final String BUCKETNAME_SUFFIX_MEETUP_IMG = "meetup_img";


    @Bean(name = "s3Client")
    public S3Client amazonS3Client() {
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                .build();
    }


}







