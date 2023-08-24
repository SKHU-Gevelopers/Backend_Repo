package site.unimeet.unimeetbackend.domain.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.api.comment.dto.CommentDto;
import site.unimeet.unimeetbackend.api.comment.dto.CommentRequestDto;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.post.PostRepository;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.domain.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public List<CommentDto> findAllComments(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(CommentDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto createComment(Long postId, CommentRequestDto requestDto, Student student) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));
        Comment comment = new Comment(requestDto.getContent(), student, post);
        commentRepository.save(comment);

        return CommentDto.toDto(comment);
    }

    public void deleteComment(Long id, Student student) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow();
        validateDeleteComment(comment, student);
        commentRepository.delete(comment);
    }

    private void validateDeleteComment(Comment comment, Student student){
        if(!comment.isOwnedComment(student)){
            throw new EntityNotFoundException(ErrorCode.STUDENT_NOT_FOUND);
        }
    }
}
