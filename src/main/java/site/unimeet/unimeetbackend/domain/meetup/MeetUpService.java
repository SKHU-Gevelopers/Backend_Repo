package site.unimeet.unimeetbackend.domain.meetup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.api.post.dto.MeetUpDetailDto;
import site.unimeet.unimeetbackend.api.post.dto.MeetUpListDto;
import site.unimeet.unimeetbackend.api.post.dto.MeetUpRequestDto;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.post.PostService;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentService;
import site.unimeet.unimeetbackend.global.config.cloud.S3Config;
import site.unimeet.unimeetbackend.global.exception.BusinessException;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.util.EntityUtil;
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
    // Todo 두 번 이상 신청할 수 없도록 예외처리
    @Transactional
    public void createMeetUpRequest(Long targetPostId, MeetUpRequestDto.Req req, String requesterEmail){
        /** post, sender, receiver와 본문 내용으로 MeetUp 객체를 생성해야 한다.*/
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

    public MeetUpListDto.Res getMeetUpList(String receiverEmail) {
        // receiver 조회
        Student receiver = studentService.findByEmail(receiverEmail);

        // receiver가 피신청자인 meetUp 목록 조회
        List<MeetUp> meetUps = meetUpRepository.findAllByReceiver(receiver);
        return MeetUpListDto.Res.from(meetUps);
    }

    /**
     * meetUp 상세정보를 조회한다.
     * meetUp의 receiver가 파라미터의 receiver(email)과 같아야 한다.
     */
    public MeetUpDetailDto.Res getMeetUpDetail(Long meetUpId, String httpRequesterEmail) {
        MeetUp meetUp = findByIdFetchAll(meetUpId);
        String receiverEmail = meetUp.getReceiver().getEmail();

        // receiver와 httpRequester가 같지 않다면 예외발생
        if (! receiverEmail.equals(httpRequesterEmail)){
            throw new BusinessException(ErrorCode.MEETUP_RECEIVER_NOT_MATCHED);
        }

        return MeetUpDetailDto.Res.from(meetUp);
    }

    public MeetUp findById(Long meetUpId) {
        return EntityUtil.checkNotFound(meetUpRepository.findById(meetUpId), ErrorCode.MEETUP_NOT_FOUND);
    }

    // Receiver와 TargetPost를 Fetch Join으로 가져온다.
    public MeetUp findByIdFetchAll(Long meetUpId) {
        return EntityUtil.checkNotFound(meetUpRepository.findByIdFetchAll(meetUpId), ErrorCode.MEETUP_NOT_FOUND);
    }
}













