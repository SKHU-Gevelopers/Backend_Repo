package site.unimeet.unimeetbackend.domain.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.api.post.dto.PostDetailDto;
import site.unimeet.unimeetbackend.api.post.dto.PostListDto;
import site.unimeet.unimeetbackend.api.post.dto.PostUploadDto;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentService;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.util.EntityUtil;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final StudentService studentService;

    public Post findById(Long id){
        return EntityUtil.checkNotFound(postRepository.findById(id), ErrorCode.POST_NOT_FOUND);
    }

    public Post findByIdFetchImageUrls(Long id){
        return EntityUtil.checkNotFound(postRepository.findByIdFetchImageUrls(id), ErrorCode.POST_NOT_FOUND);
    }

    @Transactional
    //게시글 삭제
    public void deletePost(Long id){
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




    //게시글 좋아요 기능
//    @Transactional
//    public String likePost(Long id) {
//        Optional<Post> post = postRepository.findById(id);
//        User user= userRepository.findByEmail(UserSignUpDto.getEmail());
//        if(postLikeRepository.findByPostAndUser(post,user))
//    }

}
