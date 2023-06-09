package site.unimeet.unimeetbackend.api.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.common.RspsTemplate;
import site.unimeet.unimeetbackend.api.post.dto.PostDetailDto;
import site.unimeet.unimeetbackend.api.post.dto.PostListDto;
import site.unimeet.unimeetbackend.api.post.dto.PostUploadDto;
import site.unimeet.unimeetbackend.domain.post.PostService;
import site.unimeet.unimeetbackend.global.config.cloud.S3Config;
import site.unimeet.unimeetbackend.util.S3Service;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;
    private final S3Service s3Service;

    //게시글 목록 조회
    @GetMapping("/posts")
    public RspsTemplate<PostListDto.Res> handleGetPosts(){
        PostListDto.Res postList = postService.getPosts();
        return new RspsTemplate<>(HttpStatus.OK, postList);
    }

    // 게시글 단건 조회
    @GetMapping("/posts/{id}")
    public RspsTemplate<PostDetailDto.Res> handleGetPost(@PathVariable Long id){
        PostDetailDto.Res postList = postService.getPostDetail(id);
        return new RspsTemplate<>(HttpStatus.OK, postList);
    }

    //게시글 생성
    @PostMapping("/posts")
    public ResponseEntity<RspsTemplate<String>> handleAddPost(@ModelAttribute @Valid PostUploadDto postUploadDto){
        List<String> uploadedFileUrls = s3Service.upload(postUploadDto.getPostImages(), S3Config.BUCKETNAME_SUFFIX_POST_IMG);
        postService.addPost(postUploadDto.toEntity(uploadedFileUrls));

        RspsTemplate<String> rspsTemplate = new RspsTemplate<>(HttpStatus.CREATED,"게시글 생성 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspsTemplate);
    }

    //게시글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> handleDeletePost(@PathVariable("id") Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}

    //게시글 수정

    //게시글 좋아요

    //인기글 조회

