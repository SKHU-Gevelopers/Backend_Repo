package site.unimeet.unimeetbackend.domain.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.api.comment.dto.CommentDto;
import site.unimeet.unimeetbackend.api.comment.dto.CommentRequestDto;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.post.PostRepository;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentRepository;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.domain.EntityNotFoundException;
import site.unimeet.unimeetbackend.util.EntityUtil;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final StudentRepository studentRepository;

    public List<CommentDto> findAllComments(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(CommentDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto createComment(Long postId, CommentRequestDto requestDto, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));
      
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.STUDENT_NOT_FOUND));

        Comment comment = new Comment(requestDto.getContent(), student, post);
        commentRepository.save(comment);

        return CommentDto.from(comment);
    }
  
    @Transactional
    public void deleteComment(Long id, String email) {
        Comment comment = commentRepository.findById(id)
                        .orElseThrow(()-> new EntityNotFoundException(ErrorCode.NOT_EXIST_COMMENT));
        comment.checkWriterEmail(email);
        commentRepository.delete(comment);
    }


}
