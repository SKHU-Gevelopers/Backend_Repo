package site.unimeet.unimeetbackend.domain.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.api.post.dto.PostListDto;
import site.unimeet.unimeetbackend.api.post.dto.PostUpdateDto;
import site.unimeet.unimeetbackend.api.post.dto.PostUploadDto;
import site.unimeet.unimeetbackend.domain.like.PostLike;
import site.unimeet.unimeetbackend.domain.like.PostLikeRepository;
import site.unimeet.unimeetbackend.domain.student.Student;
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
    private final PostLikeRepository postLikeRepository;
    private final StudentService studentService;

    public Post findById(Long id){
        return EntityUtil.mustNotNull(postRepository.findById(id), ErrorCode.POST_NOT_FOUND);
    }

    public Post findByIdFetchWriterAndImageUrls(Long id){
        return EntityUtil.mustNotNull(postRepository.findByIdFetchWriterAndImageUrls(id), ErrorCode.POST_NOT_FOUND);
    }

    @Transactional
    //게시글 삭제
    public void deletePost(Long id, long studentId) {
        Post post = findByIdFetchWriter(id);

        post.checkWriterId(studentId); // 게시글 작성자와 요청자가 같은지 확인
        postRepository.delete(post); // 삭제
    }

    public PostListDto.Res getPosts(Pageable pageable) {
        Slice<Post> posts = postRepository.findAllFetchWriter(pageable);
        return PostListDto.Res.from(posts);
    }

    //게시글 단건 조회
    public Post getPostDetail(Long id) {

        return findByIdFetchWriterAndImageUrls(id);
    }

    public Post findByIdFetchWriter(Long id) {
        return EntityUtil.mustNotNull(postRepository.findByIdFetchWriter(id), ErrorCode.POST_NOT_FOUND);
    }

    @Transactional
    public void writePost(PostUploadDto postUploadDto, List<String> uploadedFileUrls, long studentId) {
        Student writer = studentService.findById(studentId);
        Post post = postUploadDto.toEntity(uploadedFileUrls, writer);
        // PostUploadDto 에서 입력값을 검증하므로, ConstraintViolationException 체크하지 않음
        postRepository.save(post);
    }




    @Transactional
    //게시글 수정
    public Long editPost(Long id, PostUpdateDto postUpdateDto, long studentId, List<String> uploadedFileUrls) {
        Post post = findByIdFetchWriter(id);
        post.checkWriterId(studentId); // 게시글 작성자와 요청자가 같은지 권한 확인
        post.update(postUpdateDto.getTitle(), postUpdateDto.getContent(),postUpdateDto.getMaxPeople(), postUpdateDto.getGender(),uploadedFileUrls);
        return id;
    }

    //게시글 즐겨찾기 기능
    @Transactional
    public void updateLikePost(Long id, long studentId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));

        Student student = studentService.findById(studentId);

        if (!hasLikePost(post,student)) {
            post.increaseLikeCount();
            createLikePost(post, student);
            return;
        }

        post.decreaseLikeCount();
        removeLikePost(post, student);
    }

    private void removeLikePost(Post post , Student student) {
        PostLike postLike = postLikeRepository.findByPostAndStudent(post,student)
                .orElseThrow();

        postLikeRepository.delete(postLike);
    }

    public void createLikePost(Post post , Student student) {
        PostLike postLike = new PostLike(post, student);
        postLikeRepository.save(postLike);
    }

    public boolean hasLikePost(Post post, Student student) {
        return postLikeRepository.findByPostAndStudent(post, student)
                .isPresent();
    }


    

}



