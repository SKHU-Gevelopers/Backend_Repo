package site.unimeet.unimeetbackend.domain.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.api.post.dto.PostDetailDto;
import site.unimeet.unimeetbackend.api.post.dto.PostListDto;
import site.unimeet.unimeetbackend.api.post.dto.PostUpdateDto;
import site.unimeet.unimeetbackend.api.post.dto.PostUploadDto;
import site.unimeet.unimeetbackend.api.student.dto.UserSignUpDto;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentRepository;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.auth.AuthenticationException;
import site.unimeet.unimeetbackend.global.exception.domain.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final StudentRepository studentRepository;

    @Transactional
    //게시글 작성
    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    public Post findById(Long id) {
        return postRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    public Post findByIdFetchImageUrls(Long id) {
        return postRepository.findByIdFetchImageUrls(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));
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


    @Transactional
    //게시글 수정
    public Long editPost(Long id, PostUpdateDto postUpdateDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));

        post.update(postUpdateDto.getTitle(), postUpdateDto.getContent(),postUpdateDto.getMaxPeople(), postUpdateDto.getGender());

        return id;
    }

    //게시글 좋아요 기능
//    @Transactional
//    public String likePost(Long id){
//
//    }

}



