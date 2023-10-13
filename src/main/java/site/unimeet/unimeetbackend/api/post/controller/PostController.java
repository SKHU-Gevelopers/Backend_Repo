package site.unimeet.unimeetbackend.api.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.common.ResTemplate;
import site.unimeet.unimeetbackend.api.post.dto.*;
import site.unimeet.unimeetbackend.domain.meetup.MeetUpService;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.post.PostService;
import site.unimeet.unimeetbackend.global.config.cloud.S3Config;
import site.unimeet.unimeetbackend.global.resolver.StudentEmail;
import site.unimeet.unimeetbackend.util.PageableUtil;
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
    public ResTemplate<PostListDto.Res> handleGetPosts(@RequestParam(defaultValue = "1") final int page){
        final int POST_PAGE_SIZE = 8;
        Pageable pageable = PageableUtil.of(page, POST_PAGE_SIZE);

        PostListDto.Res postList = postService.getPosts(pageable);
        return new ResTemplate<>(HttpStatus.OK, postList);
    }

    // 게시글 단건 조회
    @GetMapping("/posts/{id}")
    public ResTemplate<PostDetailDto.Res> handleGetPost(@PathVariable Long id, @StudentEmail String email){
        Post post = postService.getPostDetail(id);
        PostDetailDto.Res resDto = PostDetailDto.Res.from(post, email);
        return new ResTemplate<>(HttpStatus.OK, resDto);

    }

    //게시글 생성
    @PostMapping("/posts")
    public ResponseEntity<ResTemplate<String>> handleAddPost(@ModelAttribute @Valid PostUploadDto postUploadDto
                                                                                                                , @StudentEmail String email){
        // 이미지 저장 후 url 리스트 반환
        List<String> uploadedFileUrls = s3Service.upload(postUploadDto.getPostImages(), S3Config.BUCKETNAME_SUFFIX_POST_IMG);
        postService.writePost(postUploadDto, uploadedFileUrls, email);

        ResTemplate<String> resTemplate = new ResTemplate<>(HttpStatus.CREATED,"게시글 생성 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(resTemplate);
    }

    //게시글 수정
    @PutMapping("posts/{id}")
    public ResponseEntity<ResTemplate<String>> handleEditPost(@PathVariable("id") Long id
                                                                                                                 , @Valid @ModelAttribute PostUpdateDto postUpdateDto
                                                                                                                 , @StudentEmail String email
    ){
        List<String> uploadedFileUrls = s3Service.upload(postUpdateDto.getPostImages(), S3Config.BUCKETNAME_SUFFIX_POST_IMG);
        postService.editPost(id, postUpdateDto, email, uploadedFileUrls);

        ResTemplate<String> resTemplate = new ResTemplate<>(HttpStatus.OK,"게시글 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(resTemplate);
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
    public ResTemplate<MeetUpListDto.Res> handleGetMeetUpList(@StudentEmail String email){
        MeetUpListDto.Res meetUpRequests = meetUpService.getMeetUpList(email);
        return new ResTemplate<>(HttpStatus.OK, meetUpRequests);
    }

    // 받은 만남신청 상세
    @GetMapping("/meet-ups/{meetUpId}")
    public ResTemplate<MeetUpDetailDto.Res> handleGetMeetUpRequestDetail(
                                                                                            @PathVariable Long meetUpId
                                                                                          , @StudentEmail String receiverEmail){
        MeetUpDetailDto.Res meetUpRequestDetail = meetUpService.getMeetUpDetail(meetUpId, receiverEmail);
        return new ResTemplate<>(HttpStatus.OK, meetUpRequestDetail);
    }

    // 만남신청 수락
    @PostMapping("/meet-ups/{meetUpId}/accept")
    public ResponseEntity<?> handleAcceptMeetUpRequest(@PathVariable Long meetUpId, @StudentEmail String email){
        meetUpService.accept(meetUpId, email);
        return ResponseEntity.noContent().build();
    }
    
    //보낸 만남신청 목록
    @GetMapping("/meet-ups/sent")
    public ResTemplate<MeetUpListDto.Res> handleSentMeetUpList(@StudentEmail String email){
        MeetUpListDto.Res sentMeetUp = meetUpService.getSendList(email);
        return new ResTemplate<>(HttpStatus.OK, sentMeetUp);
    }




}


