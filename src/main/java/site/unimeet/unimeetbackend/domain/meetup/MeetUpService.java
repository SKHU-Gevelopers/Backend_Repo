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


    // Todo 자기 자신에게는 신청할 수 없도록 예외처리
    @Transactional
    public void createMeetUpRequest(Long targetPostId, MeetUpRequestDto.Req req, String requesterEmail){
        // post 조회
        Post targetPost = postService.findByIdFetchWriter(targetPostId);

        // post의 작성자이자, meetUp의 수신자
        Student receiver = targetPost.getWriter();

        // sender 조회
        Student sender = studentService.findByEmail(requesterEmail);

        // 이미지 저장
        List<String> uploadedMeetUpImgUrls = s3Service.upload(req.getMeetUpImage(), S3Config.BUCKETNAME_SUFFIX_MEETUP_IMG);

        // 객체 생성 후 저장
        MeetUp meetUp = req.toEntity(uploadedMeetUpImgUrls, targetPost, sender, receiver);
        meetUpRepository.save(meetUp);
    }

//    public MeetUpListDto.Res getMeetUpList(String email) {
//        Student receiver = studentService.findByEmail(email);
//        meetUpRepository.findAllBy
//    }
}
