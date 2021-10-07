package okky.team.chawchaw.utils.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter @Setter
public class PageResultDto<DTO> {

    private List<DTO> contents;
    Integer totalCnt;
    Integer startPage;
    Integer endPage;
    Integer curPage;
    boolean isPrevious;
    boolean isNext;

    public PageResultDto(Page<DTO> result) {
        Pageable pageable = result.getPageable();
        contents = result.getContent();
        /* 페이지 정보 */
        curPage = pageable.getPageNumber() + 1;
        totalCnt = (int) result.getTotalElements();
        endPage = (int) (Math.ceil(curPage / 10.0) * 10);
        startPage = endPage - 9;
        endPage = Math.min(endPage, result.getTotalPages());
        endPage = (endPage == 0) ? 1 : endPage;
        isPrevious = curPage > 10;
        isNext = result.getTotalPages() / 10 != pageable.getPageNumber() / 10;
    }

    @JsonProperty(value = "isPrevious")
    public boolean isPrevious() {
        return isPrevious;
    }

    @JsonProperty(value = "isNext")
    public boolean isNext() {
        return isNext;
    }

}
