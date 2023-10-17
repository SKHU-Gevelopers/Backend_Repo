package site.unimeet.unimeetbackend.api.student.component.dm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.common.ResTemplate;
import site.unimeet.unimeetbackend.api.student.component.dm.dto.DmListDto;
import site.unimeet.unimeetbackend.api.student.component.dm.dto.ReadDMDto;
import site.unimeet.unimeetbackend.api.student.component.dm.dto.SendDMDto;
import site.unimeet.unimeetbackend.domain.student.component.dm.DmService;
import site.unimeet.unimeetbackend.global.resolver.StudentId;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class DmController {
    private final DmService dmService;

    // DM 보내기
    // {userId} 는 receiverId, 토큰으로 sender 확인
    @PostMapping("/users/{userId}/dm")
    public ResponseEntity<Void> handleSendDM(@PathVariable("userId") Long receiverId, @StudentId long loggedInId,
                                                     @RequestBody @Valid SendDMDto.Req sendDMDto) {

        dmService.sendDm(receiverId, loggedInId, sendDMDto.getTitle(), sendDMDto.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // DM 단건조회
    @GetMapping("/dm/{dmId}")
    public ResTemplate<ReadDMDto.Res> handleGetDM(@PathVariable Long dmId, @StudentId long loggedInId) {

        ReadDMDto.Res res = dmService.readDM(dmId, loggedInId);
        return new ResTemplate<>(HttpStatus.OK, res);
    }

    // DM 목록조회
    @GetMapping("/dm/received")
    public ResTemplate<DmListDto.Res> handleGetDMList(@StudentId long loggedInId) {
        DmListDto.Res res = dmService.readDMList(loggedInId);
        return new ResTemplate<>(HttpStatus.OK, res);
    }


    //보낸 DM 목록조회
    @GetMapping("/dm/sent")
    public ResTemplate<DmListDto.Res> handleSetDMList(@StudentId long loggedInId){
        DmListDto.Res sentRes = dmService.sentDMList(loggedInId);
        return new ResTemplate<>(HttpStatus.OK, sentRes);
    }

    //DM 삭제
    @DeleteMapping("/dm/inbox/{dmId}")
    public ResponseEntity<ResTemplate<Void>> deleteDM (@PathVariable Long dmId, @StudentId long loggedInId){

        dmService.deleteDm(dmId, loggedInId);
        ResTemplate<Void> resTemplate = new ResTemplate<>(HttpStatus.OK, dmId + "번 댓글 삭제 완료");
        return ResponseEntity.ok(resTemplate);
    }



}
