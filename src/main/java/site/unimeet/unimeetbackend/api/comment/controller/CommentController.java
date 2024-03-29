package site.unimeet.unimeetbackend.api.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.comment.dto.CommentDto;
import site.unimeet.unimeetbackend.api.comment.dto.CommentRequestDto;
import site.unimeet.unimeetbackend.api.common.ResTemplate;
import site.unimeet.unimeetbackend.domain.comment.CommentService;
import site.unimeet.unimeetbackend.global.resolver.StudentId;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController

public class CommentController {
    private final CommentService commentService;


    @GetMapping("/posts/{postId}/comments")
    public ResTemplate<List<CommentDto>> findAll(@PathVariable Long postId){

        List<CommentDto> commentDtos = commentService.findAllComments(postId);

        return new ResTemplate<>(HttpStatus.OK
                , "댓글목록 갯수: " + commentDtos.size()
                , commentDtos);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ResTemplate<CommentDto>> writeComment(@PathVariable Long postId
                                                                , @Valid @RequestBody final CommentRequestDto requestDto
                                                                , @StudentId long loggedInId){

        CommentDto commentDto = commentService.createComment(postId, requestDto, loggedInId);

        ResTemplate<CommentDto> resTemplate = new ResTemplate<>(HttpStatus.CREATED, "댓글 작성 완료", commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resTemplate);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ResTemplate<Void>> deleteComment(@PathVariable Long id, @StudentId long loggedInId){

        commentService.deleteComment(id, loggedInId);

        ResTemplate<Void> resTemplate = new ResTemplate<>(HttpStatus.OK, id + "번 댓글 삭제 완료");
        return ResponseEntity.ok(resTemplate);
    }
}
