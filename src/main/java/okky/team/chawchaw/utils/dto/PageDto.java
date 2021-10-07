package okky.team.chawchaw.utils.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter @Setter
public class PageDto {

    private int page = 1;
    private int size = 10;

    public PageDto(int page) {
        this.page = page;
    }

    public PageDto(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public Pageable getPageable() {
        return PageRequest.of(page -1, size);
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page -1, size, sort);
    }

}
