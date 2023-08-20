package site.unimeet.unimeetbackend.api.student.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import site.unimeet.unimeetbackend.api.common.SliceInfoDto;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.component.enums.Department;
import site.unimeet.unimeetbackend.domain.student.component.enums.Mbti;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBook;

import java.util.List;
import java.util.stream.Collectors;

// 공개 프로필(마이페이지) 조회
public class PublicMyPageDto {

    @Getter
    public static class Res{
        StudentDto student;
        List<GuestBookDto> guestBooks;
        SliceInfoDto page;

        public static Res from(Student student, Page<GuestBook> guestBooks) {
            return new Res(StudentDto.from(student), GuestBookDto.from(guestBooks), SliceInfoDto.from(guestBooks));
        }

        public Res(StudentDto student, List<GuestBookDto> guestBooks, SliceInfoDto page) {
            this.student = student;
            this.guestBooks = guestBooks;
            this.page = page;
        }

        @Getter
        @Builder
        private static class StudentDto {
            long id;
            String profileImageUrl;
            String nickname;
            Department department;
            Mbti mbti;

            static StudentDto from(Student student) {
                return StudentDto.builder()
                        .id(student.getId())
                        .profileImageUrl(student.getProfileImageUrl())
                        .nickname(student.getNickname())
                        .department(student.getDepartment())
                        .mbti(student.getMbti())
                        .build();
            }
        }

        @Getter
        @Builder
        private static class GuestBookDto{
            long id; // DB에서 가져온 값이므로 NULL이 아니고 1 이상이므로 primitive type
            long writerId;
            String profileImageUrl;
            String content;

            // GuestBookDto 변환
            static List<GuestBookDto> from(Page<GuestBook> guestBooks) {
                return guestBooks.stream()
                        .map(GuestBookDto::from)
                        .collect(Collectors.toList());
            }

            static GuestBookDto from(GuestBook guestBook) {
                return GuestBookDto.builder()
                        .id(guestBook.getId())
                        .writerId(guestBook.getWriter().getId())
                        .profileImageUrl(guestBook.getWriter().getProfileImageUrl())
                        .content(guestBook.getContent())
                        .build();
            }
        }

    }
}
