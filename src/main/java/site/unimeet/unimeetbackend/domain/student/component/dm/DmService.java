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
    public void sendDm(Long receiverId, long studentId, String title, String content) {
        Student sender = studentService.findById(studentId);
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

    // DM 객체를 가져오고, email을 토대로 receiver 혹은 sender 인지 확인한다.
    // DM ID를 랜덤으로 지정하고, 본인 email을 입력했을 수도 있음.
    public ReadDMDto.Res readDM(Long dmId, long studentId) {
        Dm dm = dmRepository.findByIdFetchSenderReceiver(dmId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DM_NOT_FOUND));

        if (! dm.isSenderOrReceiver(studentId)) {
            throw new BusinessException(ErrorCode.DM_NOT_RECEIVER_OR_SENDER);
        }

        return ReadDMDto.Res.from(dm);
    }


    public DmListDto.Res readDMList(long studentId) {
        Student student = studentService.findById(studentId);
        List<Dm> dmList = findAllBySenderFetchReceiver(student);
        return new DmListDto.Res(dmList);

    }

    //쪽지 보낸 목록조회
    public DmListDto.Res sentDMList(long studentId){
        Student student = studentService.findById(studentId);
        List<Dm> dmList = findAllByReceiverFetchSender(student);
        return new DmListDto.Res(dmList);
    }

    public List<Dm> findAllByReceiverFetchSender(Student receiver) {
        return dmRepository.findAllByReceiverFetchSender(receiver);
    }

    public List<Dm> findAllBySenderFetchReceiver(Student sender){
        return dmRepository.findAllBySenderFetchReceiver(sender);
    }

    public void deleteDm (Long dmId, long studentId){
        Dm dm = dmRepository.findById(dmId)
                .orElseThrow(()-> new BusinessException(ErrorCode.DM_NOT_FOUND));
        dm.checkReceiverDm(studentId); //받은 사람이 맞는지 확인
        dmRepository.deleteById(dmId);
    }
}
