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
import site.unimeet.unimeetbackend.global.resolver.StudentId;
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
    public ResTemplate<PostDetailDto.Res> handleGetPost(@PathVariable Long id, @StudentId long loggedInId){
        Post post = postService.getPostDetail(id);
        PostDetailDto.Res resDto = PostDetailDto.Res.from(post, loggedInId);
        return new ResTemplate<>(HttpStatus.OK, resDto);

    }

    //게시글 생성
    @PostMapping("/posts")
    public ResponseEntity<ResTemplate<String>> handleAddPost(@ModelAttribute @Valid PostUploadDto postUploadDto
                                                                                                                , @StudentId long loggedInId){
        // 이미지 저장 후 url 리스트 반환
        List<String> uploadedFileUrls = s3Service.upload(postUploadDto.getPostImages(), S3Config.BUCKETNAME_SUFFIX_POST_IMG);
        postService.writePost(postUploadDto, uploadedFileUrls, loggedInId);

        ResTemplate<String> resTemplate = new ResTemplate<>(HttpStatus.CREATED,"게시글 생성 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(resTemplate);
    }

    //게시글 수정
    @PutMapping("posts/{id}")
    public ResponseEntity<ResTemplate<String>> handleEditPost(@PathVariable("id") Long id
                                                                                                                 , @Valid @ModelAttribute PostUpdateDto postUpdateDto
                                                                                                                 , @StudentId long loggedInId
    ){
        List<String> uploadedFileUrls = s3Service.upload(postUpdateDto.getPostImages(), S3Config.BUCKETNAME_SUFFIX_POST_IMG);
        postService.editPost(id, postUpdateDto, loggedInId, uploadedFileUrls);

        ResTemplate<String> resTemplate = new ResTemplate<>(HttpStatus.OK,"게시글 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(resTemplate);
    }

    //게시글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> handleDeletePost(@PathVariable("id") Long id, @StudentId long loggedInId){
        postService.deletePost(id, loggedInId);
        return ResponseEntity.noContent().build();
    }

    //게시글 좋아요
    @PutMapping("/posts/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @StudentId long loggedInId){
        postService.updateLikePost(postId, loggedInId);
        return ResponseEntity.ok("게시글 좋아요");
    }

    // 만남 신청
    @PostMapping("/posts/{postId}/meet-ups")
    public ResponseEntity<?> handleCreateMeetUp(@PathVariable Long postId, @ModelAttribute MeetUpRequestDto.Req req,
                                                                                        @StudentId long loggedInId){
        meetUpService.createMeetUpRequest(postId, req, loggedInId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

     //받은 만남신청 목록
    @GetMapping("/meet-ups/received")
    public ResTemplate<MeetUpListDto.Res> handleGetMeetUpList(@StudentId long loggedInId){
        MeetUpListDto.Res meetUpRequests = meetUpService.getMeetUpList(loggedInId);
        return new ResTemplate<>(HttpStatus.OK, meetUpRequests);
    }

    // 받은 만남신청 상세
    @GetMapping("/meet-ups/{meetUpId}")
    public ResTemplate<MeetUpDetailDto.Res> handleGetMeetUpRequestDetail(
                                                                                            @PathVariable Long meetUpId
                                                                                          , @StudentId long loggedInId){
        MeetUpDetailDto.Res meetUpRequestDetail = meetUpService.getMeetUpDetail(meetUpId, loggedInId);
        return new ResTemplate<>(HttpStatus.OK, meetUpRequestDetail);
    }

    // 만남신청 수락
    @PostMapping("/meet-ups/{meetUpId}/accept")
    public ResponseEntity<?> handleAcceptMeetUpRequest(@PathVariable Long meetUpId, @StudentId long loggedInId){
        meetUpService.accept(meetUpId, loggedInId);
        return ResponseEntity.noContent().build();
    }
    
    //보낸 만남신청 목록
    @GetMapping("/meet-ups/sent")
    public ResTemplate<MeetUpListDto.Res> handleSentMeetUpList(@StudentId long loggedInId){
        MeetUpListDto.Res sentMeetUp = meetUpService.getSendList(loggedInId);
        return new ResTemplate<>(HttpStatus.OK, sentMeetUp);
    }




}


