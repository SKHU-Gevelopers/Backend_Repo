package site.unimeet.unimeetbackend.api.student.component.dm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.common.RspsTemplate;
import site.unimeet.unimeetbackend.api.student.component.dm.dto.ReadDMDto;
import site.unimeet.unimeetbackend.api.student.component.dm.dto.SendDMDto;
import site.unimeet.unimeetbackend.domain.student.component.dm.DMService;
import site.unimeet.unimeetbackend.global.resolver.StudentEmail;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class DMController {
    private final DMService dmService;

    // DM 보내기
    // {userId} 는 receiverId, 토큰으로 sender 확인
    @PostMapping("/users/{userId}/dm")
    public ResponseEntity<?> handleSendDM(@PathVariable("userId") Long senderId, @StudentEmail String receiverEmail,
                                                     @RequestBody @Valid SendDMDto.Req sendDMDto) {

        dmService.sendDM(senderId, receiverEmail, sendDMDto.getTitle(), sendDMDto.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // DM 단건조회
    @GetMapping("/dm/{dmId}")
    public RspsTemplate<ReadDMDto.Res> handleGetDM(@PathVariable Long dmId, @StudentEmail String email) {

        ReadDMDto.Res res = dmService.readDM(dmId, email);
        return new RspsTemplate<>(HttpStatus.OK, res);
    }



    // DM 목록조회


}
