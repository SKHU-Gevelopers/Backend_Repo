package site.unimeet.unimeetbackend.api.student.component.dm.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.common.ResTemplate;
import site.unimeet.unimeetbackend.api.student.component.dm.dto.DmListDto;
import site.unimeet.unimeetbackend.api.student.component.dm.dto.ReadDMDto;
import site.unimeet.unimeetbackend.api.student.component.dm.dto.SendDMDto;
import site.unimeet.unimeetbackend.domain.student.component.dm.DmService;
import site.unimeet.unimeetbackend.global.resolver.StudentEmail;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class DmController {
    private final DmService dmService;

    // DM 보내기
    // {userId} 는 receiverId, 토큰으로 sender 확인
    @PostMapping("/users/{userId}/dm")
    public ResponseEntity<?> handleSendDM(@PathVariable("userId") Long receiverId, @StudentEmail String senderEmail,
                                                     @RequestBody @Valid SendDMDto.Req sendDMDto) {

        dmService.sendDm(receiverId, senderEmail, sendDMDto.getTitle(), sendDMDto.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // DM 단건조회
    @GetMapping("/dm/{dmId}")
    public ResTemplate<ReadDMDto.Res> handleGetDM(@PathVariable Long dmId, @StudentEmail String email) {

        ReadDMDto.Res res = dmService.readDM(dmId, email);
        return new ResTemplate<>(HttpStatus.OK, res);
    }

    // DM 목록조회
    @GetMapping("/dm")
    public ResTemplate<DmListDto.Res> handleGetDMList(@StudentEmail String email) {
        DmListDto.Res res = dmService.readDMList(email);
        return new ResTemplate<>(HttpStatus.OK, res);
    }


    //보낸 DM 목록조회
    @GetMapping("/dm/sent")
    public ResTemplate<DmListDto.Res> handleSetDMList(@StudentEmail String email){
        DmListDto.Res sentRes = dmService.sentDMList(email);
        return new ResTemplate<>(HttpStatus.OK, sentRes);
    }

    //DM 삭제
    @DeleteMapping("/dm/{dmId}")
    public ResponseEntity<ResTemplate<?>> deleteDM (@PathVariable Long dmId, @StudentEmail String email){

        dmService.deleteDm(dmId, email);
        ResTemplate<?> resTemplate = new ResTemplate<>(HttpStatus.OK, dmId + "번 댓글 삭제 완료");
        return ResponseEntity.ok(resTemplate);

    }



}
