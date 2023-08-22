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

    @PostMapping("/comments")
    public ResponseEntity writeComment(@Valid @RequestBody final CommentRequestDto requestDto, @StudentEmail Student student){
        return ResponseEntity.ok(commentService.createComment(requestDto,student));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id, @StudentEmail Student student){
        commentService.deleteComment(id, student);
        return ResponseEntity.ok("댓글 삭제 완료");
    }
}
