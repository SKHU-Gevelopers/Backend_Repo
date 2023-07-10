package site.unimeet.unimeetbackend.domain.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.domain.EntityNotFoundException;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    //게시글 작성
    public Post addPost(Post post){
       return postRepository.save(post);
    }

    public Post findById(Long id){
        return postRepository.findById(id).
                orElseThrow(()->new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    @Transactional
    //게시글 삭제
    public void deletePost(Long id){
        Post post = findById(id);
        postRepository.delete(post);
    }

    //게시글 좋아요 기능
//    @Transactional
//    public String likePost(Long id) {
//        Optional<Post> post = postRepository.findById(id);
//        User user= userRepository.findByEmail(UserSignUpDto.getEmail());
//        if(postLikeRepository.findByPostAndUser(post,user))
//    }

}
