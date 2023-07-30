package site.unimeet.unimeetbackend.domain.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.api.post.dto.*;
import site.unimeet.unimeetbackend.domain.like.PostLike;
import site.unimeet.unimeetbackend.domain.like.PostLikeRepository;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentRepository;
import site.unimeet.unimeetbackend.domain.student.StudentService;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.domain.EntityNotFoundException;
import site.unimeet.unimeetbackend.util.EntityUtil;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final StudentRepository studentRepository;
    private final PostLikeRepository postLikeRepository;
    private final StudentService studentService;

    public Post findById(Long id){
        return EntityUtil.checkNotFound(postRepository.findById(id), ErrorCode.POST_NOT_FOUND);
    }

    public Post findByIdFetchImageUrls(Long id){
        return EntityUtil.checkNotFound(postRepository.findByIdFetchImageUrls(id), ErrorCode.POST_NOT_FOUND);
    }

    @Transactional
    //게시글 삭제
    public void deletePost(Long id) {
        Post post = findById(id);
        postRepository.delete(post);
    }

    public PostListDto.Res getPosts() {
        List<Post> posts = postRepository.findAll();
        return PostListDto.Res.from(posts);
    }

    public PostDetailDto.Res getPostDetail(Long id) {
        Post post = findByIdFetchImageUrls(id);
        return PostDetailDto.Res.from(post);
    }

    public Post findByIdFetchWriter(Long id) {
        return EntityUtil.checkNotFound(postRepository.findByIdFetchWriter(id), ErrorCode.POST_NOT_FOUND);
    }

    @Transactional
    public void writePost(PostUploadDto postUploadDto, List<String> uploadedFileUrls, String email) {
        Student writer = studentService.findByEmail(email);
        Post post = postUploadDto.toEntity(uploadedFileUrls, writer);
        // PostUploadDto 에서 입력값을 검증하므로, ConstraintViolationException 체크하지 않음
        postRepository.save(post);
    }




    @Transactional
    //게시글 수정
    public Long editPost(Long id, PostUpdateDto postUpdateDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));

        post.update(postUpdateDto.getTitle(), postUpdateDto.getContent(),postUpdateDto.getMaxPeople(), postUpdateDto.getGender());

        return id;
    }

    //게시글 좋아요 기능
    @Transactional
    public String updateLikePost(PostLikeDto postLikeDto) {

        Post post = postRepository.findById(postLikeDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));

        Student student = studentRepository.findById(postLikeDto.getStudentId())
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.STUDENT_NOT_FOUND));

        if (!hasLikePost(post,student)) {
            post.increaseLikeCount();
            return createLikePost(post, student);
        }

        post.decreaseLikeCount();
        return removeLikePost(post, student);
    }

    private String removeLikePost(Post post , Student student) {
        PostLike postLike = postLikeRepository.findByPostAndStudent(post,student)
                .orElseThrow();

        postLikeRepository.delete(postLike);

        return "게시글 좋아요 삭제";
    }

    public String createLikePost(Post post , Student student) {
        PostLike postLike = new PostLike(post, student);
        postLikeRepository.save(postLike);
        return "게시글 좋아요";
    }

    public boolean hasLikePost(Post post, Student student) {
        return postLikeRepository.findByPostAndStudent(post, student)
                .isPresent();
    }


    

}



