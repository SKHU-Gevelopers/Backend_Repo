package site.unimeet.unimeetbackend.api.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.comment.dto.CommentReadDto;
import site.unimeet.unimeetbackend.api.comment.dto.CommentRequestDto;
import site.unimeet.unimeetbackend.api.student.dto.MyPageDto;
import site.unimeet.unimeetbackend.domain.comment.Comment;
import site.unimeet.unimeetbackend.domain.comment.CommentRepository;
import site.unimeet.unimeetbackend.domain.comment.CommentService;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.global.resolver.StudentEmail;

import javax.validation.Valid;
@RequiredArgsConstructor
@RestController

public class CommentController {
    private final CommentService commentService;


    @GetMapping("/comments")
    public ResponseEntity findAll(@Valid CommentReadDto commentReadDto){
        return ResponseEntity.ok(commentService.findAllComments(commentReadDto));
    }

<<<<<<< Updated upstream
    @PostMapping("/comments")
    public ResponseEntity writeComment(@Valid @RequestBody final CommentRequestDto requestDto, @StudentEmail Student student){
        return ResponseEntity.ok(commentService.createComment(requestDto,student));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id, @StudentEmail Student student){
        commentService.deleteComment(id, student);
        return ResponseEntity.ok("댓글 삭제 완료");
=======
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ResTemplate<CommentDto>> writeComment(@PathVariable Long postId
                                                                , @Valid @RequestBody final CommentRequestDto requestDto
                                                                , @StudentEmail String email){

        CommentDto commentDto = commentService.createComment(postId, requestDto, email);

        ResTemplate<CommentDto> resTemplate = new ResTemplate<>(HttpStatus.CREATED, "댓글 작성 완료", commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resTemplate);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ResTemplate<?>> deleteComment(@PathVariable Long id, @StudentEmail String email){

        commentService.deleteComment(id, email);

        ResTemplate<?> resTemplate = new ResTemplate<>(HttpStatus.OK, id + "번 댓글 삭제 완료");
        return ResponseEntity.ok(resTemplate);
>>>>>>> Stashed changes
    }
}
