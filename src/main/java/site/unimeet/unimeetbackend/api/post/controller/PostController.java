package site.unimeet.unimeetbackend.api.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.common.RspsTemplate;
import site.unimeet.unimeetbackend.api.common.SingleRspsTemplate;
import site.unimeet.unimeetbackend.api.post.dto.PostDto;
import site.unimeet.unimeetbackend.api.post.dto.PostListDto;
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
    public RspsTemplate<Post> getPost(){
        List<Post> postList = postRepository.findAll();
        return new RspsTemplate<>(200, postList);
    }

    //게시글 생성
    @PostMapping("/post")
    public ResponseEntity<SingleRspsTemplate<String>> addPost(@RequestBody PostDto postDto){
        postService.addPost(postDto);
        SingleRspsTemplate<String> rspsTemplate = new SingleRspsTemplate<>(200,"게시글 생성 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspsTemplate);
    }
    //게시글 삭제
    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id){
        if(id == null){
            return ResponseEntity.ok("존재하지 않는 게시글입니다");
        }
        else{
            postService.deletePost(id);
            return ResponseEntity.ok("해당 게시글 삭제 완료");
        }
    }

    //게시글 수정

    //게시글 좋아요

    //인기글 조회
}
