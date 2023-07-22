package site.unimeet.unimeetbackend.domain.student.component.dm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.api.student.component.dm.dto.DMListDto;
import site.unimeet.unimeetbackend.api.student.component.dm.dto.ReadDMDto;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentService;
import site.unimeet.unimeetbackend.global.exception.BusinessException;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DMService {
    private final DMRepository dmRepository;
    private final StudentService studentService;

    @Transactional
    public void sendDM(Long receiverId, String senderEmail, String title, String content) {
        Student sender = studentService.findByEmail(senderEmail);
        Student receiver = studentService.findById(receiverId);

        DM dm = DM.builder()
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
        DM dm = dmRepository.findByIdFetchSenderReceiver(dmId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DM_NOT_FOUND));

        String receiverEmail = dm.getReceiver().getEmail();

        // 토큰 email과 receiverEmail이 같은지 검증
        if (! receiverEmail.equals(email)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        return ReadDMDto.Res.from(dm);
    }


    public DMListDto.Res readDMList(String email) {
        Student student = studentService.findByEmail(email);
        List<DM> dmList = findAllByReceiverFetchSender(student);
        return new DMListDto.Res(dmList);

    }

    public List<DM> findAllByReceiverFetchSender(Student receiver) {
        return dmRepository.findAllByReceiverFetchSender(receiver);
    }
}
