package site.unimeet.unimeetbackend.api.student.dto;

import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.component.enums.Department;
import site.unimeet.unimeetbackend.domain.student.component.enums.Mbti;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBook;

import java.util.List;
import java.util.stream.Collectors;

// 공개 프로필(마이페이지) 조회
public class PublicMyPageDto {

    @Getter
    @Builder
    public static class Res{
        private StudentDto student;
        private List<GuestBookDto> guestBooks;

        public static Res from(Student student, List<GuestBook> guestBooks) {
            StudentDto studentDto = StudentDto.builder()
                    .profileImageUrl(student.getProfileImageUrl())
                    .nickname(student.getNickname())
                    .department(student.getDepartment())
                    .mbti(student.getMbti())
                    .build();

            List<GuestBookDto> guestBookDtos = GuestBookDto.from(guestBooks);

            // todo guestBooks Dto 변환
            return Res.builder()
                    .student(studentDto)
                    .guestBooks(guestBookDtos)
                    .build();
        }

        @Getter
        @Builder
        private static class StudentDto {
            String profileImageUrl;
            String nickname;
            Department department;
            Mbti mbti;
        }

        @Getter
        @Builder
        private static class GuestBookDto{
            Long writerId;
            String profileImageUrl;
            String content;

            // GuestBookDto 변환
            public static List<GuestBookDto> from(List<GuestBook> guestBooks) {
                return guestBooks.stream()
                        .map(GuestBookDto::from)
                        .collect(Collectors.toList());
            }

            public static GuestBookDto from(GuestBook guestBook) {
                return GuestBookDto.builder()
                        .writerId(guestBook.getWriter().getId())
                        .profileImageUrl(guestBook.getWriter().getProfileImageUrl())
                        .content(guestBook.getContent())
                        .build();
            }
        }

    }
}
