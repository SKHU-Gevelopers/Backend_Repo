package site.unimeet.unimeetbackend.domain.post;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.api.post.dto.PostDto;
import site.unimeet.unimeetbackend.domain.like.PostLikeRepository;
import site.unimeet.unimeetbackend.domain.student.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final StudentRepository studentRepository;

    //게시글 작성
    public Post addPost(PostDto postDto){
       return postRepository.save(postDto.toEntity());
    }

    //게시글 삭제
    public void deletePost(Long id){
        postRepository.deleteById(id);
    }

    //게시글 좋아요 기능
//    @Transactional
//    public String likePost(Long id) {
//        Optional<Post> post = postRepository.findById(id);
//        User user= userRepository.findByEmail(UserSignUpDto.getEmail());
//        if(postLikeRepository.findByPostAndUser(post,user))
//    }

}
