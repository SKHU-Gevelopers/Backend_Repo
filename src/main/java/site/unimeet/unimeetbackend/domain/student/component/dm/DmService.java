package site.unimeet.unimeetbackend.domain.student.component.dm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.api.student.component.dm.dto.DmListDto;
import site.unimeet.unimeetbackend.api.student.component.dm.dto.ReadDMDto;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentService;
import site.unimeet.unimeetbackend.global.exception.BusinessException;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DmService {
    private final DmRepository dmRepository;
    private final StudentService studentService;

    @Transactional
    public void sendDm(Long receiverId, String senderEmail, String title, String content) {
        Student sender = studentService.findByEmail(senderEmail);
        Student receiver = studentService.findById(receiverId);

        Dm dm = Dm.builder()
                .title(title)
                .content(content)
                .sender(sender)
                .receiver(receiver)
                .build();

        dmRepository.save(dm);
    }

    @Transactional
    public void sendDmOnMeetUpAcceptance(Long receiverId, Long senderId) {
        // meetUpService.accept() 에서 간접호출된다면 jpa 캐시에서 조회되므로 성능부담 적음
        Student sender = studentService.findById(senderId);
        Student receiver = studentService.findById(receiverId);

        String title = sender.getNickname() + "님이 만남 신청을 수락했습니다";
        String content = sender.getNickname() + "님이 만남 신청을 수락했습니다.\n"
                    + "수락한놈 카카오톡 ID : " + sender.getKakaoId() + "\n\n\n"
                    + "이 메시지는 시스템에 의해 자동으로 발송되었습니다."
                ;

        Dm dm = Dm.builder()
                .title(title)
                .content(content)
                .sender(sender)
                .receiver(receiver)
                .build();

        dmRepository.save(dm);
    }

    // DM 객체를 가져오고, email을 토대로 receiver가 같은지 확인한다.
    // DM ID를 랜덤으로 지정하고, 본인 email을 입력했을 수도 있음.
    public ReadDMDto.Res readDM(Long dmId, String email) {
        Dm dm = dmRepository.findByIdFetchSenderReceiver(dmId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DM_NOT_FOUND));

        String receiverEmail = dm.getReceiver().getEmail();

        // 토큰 email과 receiverEmail이 같은지 검증
        if (! receiverEmail.equals(email)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        return ReadDMDto.Res.from(dm);
    }


    public DmListDto.Res readDMList(String email) {
        Student student = studentService.findByEmail(email);
        List<Dm> dmList = findAllByReceiverFetchSender(student);
        return new DmListDto.Res(dmList);

    }

    public List<Dm> findAllByReceiverFetchSender(Student receiver) {
        return dmRepository.findAllByReceiverFetchSender(receiver);
    }
}
