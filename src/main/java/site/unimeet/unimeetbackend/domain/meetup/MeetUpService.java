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
import site.unimeet.unimeetbackend.domain.student.component.dm.DmService;
import site.unimeet.unimeetbackend.global.config.cloud.S3Config;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.util.EmailService;
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
    private final DmService dmService;
    private final EmailService emailService;

    // 만남 신청
    @Transactional
    public void createMeetUpRequest(Long targetPostId, MeetUpRequestDto.Req req, String requesterEmail){
        /** post, sender, receiver와 본문 내용으로 MeetUp 객체를 생성해야 한다.*/
        // post 조회
        Post targetPost = postService.findByIdFetchWriter(targetPostId);

        // post의 작성자이자, meetUp의 수신자
        Student receiver = targetPost.getWriter();

        // sender 조회
        Student sender = studentService.findByEmail(requesterEmail);

        // 같은 게시글에 대해서 신청자 중복 체크
        EntityUtil.mustNull(meetUpRepository.findByTargetPostAndSender(targetPost, sender),
                ErrorCode.MEETUP_SENDER_DUPLICATED);

        // 이미지 저장
        List<String> uploadedMeetUpImgUrls = s3Service.upload(req.getMeetUpImage(), S3Config.BUCKETNAME_SUFFIX_MEETUP_IMG);

        // 객체 생성 후 저장. 객체 생성할 때 신청자와 피신청자가 같은지 체크.
        MeetUp meetUp = req.toEntity(uploadedMeetUpImgUrls, targetPost, sender, receiver);
        meetUpRepository.save(meetUp);
    }

    @Transactional
    public void accept(Long meetUpId, String httpRequesterEmail) {
        MeetUp meetUp = findByIdFetchReceiverAndSenderAndPost(meetUpId);
        Student meetUpSender = meetUp.getSender();
        Student meetUpReceiver = meetUp.getReceiver();
        Post post = meetUp.getTargetPost();

        // MeetUp receiver와 HttpRequester가 같지 않다면 예외발생
        post.checkWriterEmail(httpRequesterEmail);

        // 게시글의 상태를 DONE으로 변경한다.
        post.setStateDone();

        // todo 쪽지와 이메일로 수락알림 + 톡디보내기
        // (meetUp의) receiver가 sender에게 쪽지를 보낸다.
        dmService.sendDmOnMeetUpAcceptance(meetUpSender.getId(), meetUpReceiver.getId());

        // system에서 meetUp sender에게 이메일 전송
        String title = "Unimeet - 만남 수락 알림";
        String content = meetUpSender.getNickname() + "님이 만남 신청을 수락했습니다.\n"
                + "수락한놈 카카오톡 ID : " + meetUpSender.getKakaoId() + "\n\n\n"
                + "이 메시지는 시스템에 의해 자동으로 발송되었습니다."
                ;
        emailService.sendEmail(meetUpSender.getEmail(), title, content);
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

        // MeetUp receiver와 HttpRequester가 같지 않다면 예외발생
        meetUp.checkReceiverEmail(httpRequesterEmail);
        return MeetUpDetailDto.Res.from(meetUp);
    }

    public MeetUp findById(Long meetUpId) {
        return EntityUtil.checkNotFound(meetUpRepository.findById(meetUpId), ErrorCode.MEETUP_NOT_FOUND);
    }

    // Receiver와 TargetPost를 Fetch Join으로 가져온다.
    public MeetUp findByIdFetchAll(Long meetUpId) {
        return EntityUtil.checkNotFound(meetUpRepository.findByIdFetchAll(meetUpId), ErrorCode.MEETUP_NOT_FOUND);
    }

    public MeetUp findByIdFetchReceiverAndSenderAndPost(Long meetUpId) {
        return EntityUtil.checkNotFound(meetUpRepository.findByIdFetchReceiverAndSenderAndPost(meetUpId), ErrorCode.MEETUP_NOT_FOUND);
    }


}













