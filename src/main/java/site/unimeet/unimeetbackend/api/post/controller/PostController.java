package site.unimeet.unimeetbackend.api.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.common.RspsTemplate;
import site.unimeet.unimeetbackend.api.post.dto.*;
import site.unimeet.unimeetbackend.domain.meetup.MeetUpService;
import site.unimeet.unimeetbackend.domain.post.PostService;
import site.unimeet.unimeetbackend.global.config.cloud.S3Config;
import site.unimeet.unimeetbackend.global.resolver.StudentEmail;
import site.unimeet.unimeetbackend.util.S3Service;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;
    private final S3Service s3Service;
    private final MeetUpService meetUpService;

    //게시글 목록 조회
    @GetMapping("/posts")
    public RspsTemplate<PostListDto.Res> handleGetPosts(@RequestParam(defaultValue = "1") final int page){

        if (page < 1)
            throw new IllegalArgumentException("page는 1 이상이어야 합니다.");

        // 0-base인 페이지를 클라이언트단에서 1-based인 것처럼 사용할 수 있게 함
        final int POST_PAGE_SIZE = 6;

        Pageable pageable = PageRequest.of(page - 1 , POST_PAGE_SIZE);

        PostListDto.Res postList = postService.getPosts(pageable);
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
    public ResponseEntity<RspsTemplate<String>> handleAddPost(@ModelAttribute @Valid PostUploadDto postUploadDto
                                                                                                                , @StudentEmail String email){
        // 이미지 저장 후 url 리스트 반환
        List<String> uploadedFileUrls = s3Service.upload(postUploadDto.getPostImages(), S3Config.BUCKETNAME_SUFFIX_POST_IMG);
        postService.writePost(postUploadDto, uploadedFileUrls, email);

        RspsTemplate<String> rspsTemplate = new RspsTemplate<>(HttpStatus.CREATED,"게시글 생성 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspsTemplate);
    }

    //게시글 수정
    @PutMapping("posts/{id}")
    public ResponseEntity<RspsTemplate<String>> handleEditPost(@PathVariable("id") Long id
                                                                                                                 , @Valid @ModelAttribute PostUpdateDto postUpdateDto
                                                                                                                 , @StudentEmail String email
    ){
        List<String> uploadedFileUrls = s3Service.upload(postUpdateDto.getPostImages(), S3Config.BUCKETNAME_SUFFIX_POST_IMG);
        postService.editPost(id,postUpdateDto, email);

        RspsTemplate<String> rspsTemplate = new RspsTemplate<>(HttpStatus.OK,"게시글 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(rspsTemplate);
    }

    //게시글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> handleDeletePost(@PathVariable("id") Long id, @StudentEmail String email){
        postService.deletePost(id, email);
        return ResponseEntity.noContent().build();
    }

    //게시글 좋아요
    @PutMapping("/posts/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @StudentEmail String email){
        postService.updateLikePost(postId, email);
        return ResponseEntity.ok("게시글 좋아요");
    }

    // 만남 신청
    @PostMapping("/posts/{postId}/meet-ups")
    public ResponseEntity<?> handleCreateMeetUp(@PathVariable Long postId, @ModelAttribute MeetUpRequestDto.Req req,
                                                                                        @StudentEmail String email){
        meetUpService.createMeetUpRequest(postId, req, email);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

     //받은 만남신청 목록
    @GetMapping("/meet-ups")
    public RspsTemplate<MeetUpListDto.Res> handleGetMeetUpList(@StudentEmail String email){
        MeetUpListDto.Res meetUpRequests = meetUpService.getMeetUpList(email);
        return new RspsTemplate<>(HttpStatus.OK, meetUpRequests);
    }

    // 받은 만남신청 상세
    @GetMapping("/meet-ups/{meetUpId}")
    public RspsTemplate<MeetUpDetailDto.Res> handleGetMeetUpRequestDetail(
                                                                                            @PathVariable Long meetUpId
                                                                                          , @StudentEmail String receiverEmail){
        MeetUpDetailDto.Res meetUpRequestDetail = meetUpService.getMeetUpDetail(meetUpId, receiverEmail);
        return new RspsTemplate<>(HttpStatus.OK, meetUpRequestDetail);
    }

    // 만남신청 수락
    @PostMapping("/meet-ups/{meetUpId}/accept")
    public ResponseEntity<?> handleAcceptMeetUpRequest(@PathVariable Long meetUpId, @StudentEmail String email){
        meetUpService.accept(meetUpId, email);
        return ResponseEntity.noContent().build();
    }




}


