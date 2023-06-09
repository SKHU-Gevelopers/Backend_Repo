package site.unimeet.unimeetbackend.global.config.cloud;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private static String defaultProfileImageUrl;
    @Autowired
    public S3Config(@Value("${default.profile.image.url}") String DEFAULT_PROFILE_IMG_URL) {
        S3Config.defaultProfileImageUrl = DEFAULT_PROFILE_IMG_URL;
    }
    public static String getDefaultProfileImageUrl() {
        return defaultProfileImageUrl;
    }

    public static final String BUCKETNAME_SUFFIX_PROFILE_IMG = "/user_profile_img";
    public static final String BUCKETNAME_SUFFIX_POST_IMG = "/post_img";


    @Bean(name = "s3Client")
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey,secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }


}







