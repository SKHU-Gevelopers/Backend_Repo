package site.unimeet.unimeetbackend.domain.meetup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.api.post.dto.MeetUpRequestDto;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.post.PostService;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentService;
import site.unimeet.unimeetbackend.global.config.cloud.S3Config;
import site.unimeet.unimeetbackend.util.S3Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MeetUpService {
    private final MeetUpRepository meetUpRepository;
    private final S3Service s3Service;
    private final PostService postService;
    private final StudentService studentService;


    @Transactional
    public void createMeetUpRequest(Long targetPostId, MeetUpRequestDto.Req req, String requesterEmail){
        // post 조회
        Post targetPost = postService.findById(targetPostId);

        // requester 조회
        Student requester = studentService.findByEmail(requesterEmail);

        // 이미지 저장
        List<String> uploadedMeetUpImgUrls = s3Service.upload(req.getMeetUpImage(), S3Config.BUCKETNAME_SUFFIX_MEETUP_IMG);

        // 객체 생성 후 저장
        MeetUp meetUp = req.toEntity(uploadedMeetUpImgUrls, targetPost, requester);
        meetUpRepository.save(meetUp);
    }
}
