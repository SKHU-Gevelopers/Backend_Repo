package site.unimeet.unimeetbackend.api.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class SliceInfoDto {

    int currentPage; // 현재 페이지 번호
    int size;               // 페이지당 기본 사이즈
    boolean hasNext;    // 다음 페이지 존재 여부
    boolean hasPrevious;   // 이전 페이지 존재 여부
    boolean isFirst;   // 첫번째 페이지 여부
    boolean isLast;   // 마지막 페이지 여부
    int numberOfElements;  // 현재 페이지의 데이터 수

    public static SliceInfoDto from(Slice<?> slice) {
        return SliceInfoDto.builder()
                .currentPage(slice.getNumber() + 1) // zero-based index이므로 1을 더해줌
                .size(slice.getSize())
                .hasNext(slice.hasNext())
                .hasPrevious(slice.hasPrevious())
                .isFirst(slice.isFirst())
                .isLast(slice.isLast())
                .numberOfElements(slice.getNumberOfElements())
                .build();
    }
}
