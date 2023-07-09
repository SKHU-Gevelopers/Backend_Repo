package site.unimeet.unimeetbackend.api.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.common.RspsTemplate;
import site.unimeet.unimeetbackend.api.post.dto.PostDto;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.post.PostRepository;
import site.unimeet.unimeetbackend.domain.post.PostService;

import java.util.List;

@RequiredArgsConstructor
@RestController

public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;

    //게시글 전체 불러오기
    @GetMapping("/post")
    public RspsTemplate<List<Post>> handleGetPost(){
        List<Post> postList = postRepository.findAll();
        return new RspsTemplate<>(HttpStatus.OK, postList);
    }

    //게시글 생성
    @PostMapping("/post")
    public ResponseEntity<RspsTemplate<String>> handleAddPost(@RequestBody PostDto postDto){
        postService.addPost(postDto);
        RspsTemplate<String> rspsTemplate = new RspsTemplate<>(HttpStatus.CREATED,"게시글 생성 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspsTemplate);
    }
    //게시글 삭제
    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> handleDeletePost(@PathVariable("id") Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}

    //게시글 수정

    //게시글 좋아요

    //인기글 조회

