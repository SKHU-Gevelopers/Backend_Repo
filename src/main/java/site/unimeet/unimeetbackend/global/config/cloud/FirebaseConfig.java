package site.unimeet.unimeetbackend.global.config.cloud;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import site.unimeet.unimeetbackend.global.exception.AppServiceException;

import java.io.FileNotFoundException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    private final String FIREBASE_ADMIN_SDK_PATH;
    private final String FIREBASE_PROJECT_ID;

    @Autowired
    public FirebaseConfig(
            @Value("firebase.admin-sdk.config")String adminJsonPath,
            @Value("firebase.admin-sdk.project-id")String projectId
    ) {
        this.FIREBASE_ADMIN_SDK_PATH = adminJsonPath;
        this.FIREBASE_PROJECT_ID = projectId;
    }

    @Bean
    public FirebaseApp firebaseApp() throws Exception {
        try (InputStream serviceAccount = new ClassPathResource(FIREBASE_ADMIN_SDK_PATH).getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(FIREBASE_PROJECT_ID)
                    .build();

            return FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException e) {
            throw new AppServiceException("Firebase Admin SDK 인증 파일을 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }



}
