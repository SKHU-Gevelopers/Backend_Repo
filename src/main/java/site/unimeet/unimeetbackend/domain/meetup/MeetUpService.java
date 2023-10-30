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
    private final DmService dmService;

    // 만남 신청
    @Transactional
    public void createMeetUpRequest(Long targetPostId, MeetUpRequestDto.Req req, long studentId){
        // post, sender, receiver와 본문 내용으로 MeetUp 객체를 생성해야 한다.
        // post 조회
        Post targetPost = postService.findByIdFetchWriter(targetPostId);

        // post의 작성자이자, meetUp의 수신자
        Student receiver = targetPost.getWriter();

        // sender 조회
        Student sender = studentService.findById(studentId);

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
    public void accept(Long meetUpId, long studentId) {
        MeetUp meetUp = findByIdFetchReceiverAndSenderAndPost(meetUpId);
        Student meetUpSender = meetUp.getSender();
        Student meetUpReceiver = meetUp.getReceiver();
        Post post = meetUp.getTargetPost();

        // MeetUp receiver와 HttpRequester가 같지 않다면 예외발생
        post.checkWriterId(studentId);

        // 게시글의 상태를 DONE으로 변경한다.
        post.setStateDone();

        // (meetUp의) receiver가 sender에게 쪽지를 보낸다.
        dmService.sendDmOnMeetUpAcceptance(meetUpSender.getId(), meetUpReceiver.getId());
    }

    public MeetUpListDto.Res getMeetUpList(long receiverId) {
        // receiver 조회
        Student receiver = studentService.findById(receiverId);

        // receiver가 피신청자인 meetUp 목록 조회
        List<MeetUp> meetUps = meetUpRepository.findAllByReceiver(receiver);
        return MeetUpListDto.Res.from(meetUps);
    }
    
    //보낸 만남 신청 리스트
    public MeetUpListDto.Res getSendList(long senderId){
        //sender 조회
        Student sender = studentService.findById(senderId);

        List<MeetUp> sendMeetUps = meetUpRepository.findAllBySender(sender);
        return MeetUpListDto.Res.from(sendMeetUps);
    }

    /**
     * meetUp 상세정보를 조회한다.
     * meetUp의 receiver or sender 가 파라미터의 사용자 id와 같아야 한다.
     */
    public MeetUpDetailDto.Res getMeetUpDetail(Long meetUpId, long studentId) {
        MeetUp meetUp = findByIdFetchAll(meetUpId);

        // MeetUp receiver와 HttpRequester가 같지 않다면 예외발생
        if (! meetUp.isSenderOrReceiver(studentId)) {
            throw new BusinessException(ErrorCode.MEETUP_NOT_RECEIVER_OR_SENDER);
        }

        return MeetUpDetailDto.Res.from(meetUp);
    }

    public MeetUp findById(Long meetUpId) {
        return EntityUtil.mustNotNull(meetUpRepository.findById(meetUpId), ErrorCode.MEETUP_NOT_FOUND);
    }

    // Receiver와 TargetPost를 Fetch Join으로 가져온다.
    public MeetUp findByIdFetchAll(Long meetUpId) {
        return EntityUtil.mustNotNull(meetUpRepository.findByIdFetchAll(meetUpId), ErrorCode.MEETUP_NOT_FOUND);
    }

    public MeetUp findByIdFetchReceiverAndSenderAndPost(Long meetUpId) {
        return EntityUtil.mustNotNull(meetUpRepository.findByIdFetchReceiverAndSenderAndPost(meetUpId), ErrorCode.MEETUP_NOT_FOUND);
    }


}













