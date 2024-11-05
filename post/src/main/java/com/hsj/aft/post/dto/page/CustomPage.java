package com.hsj.aft.post.dto.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPage<T> {

    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;

    // Page를 CustomPage로 변환하는 정적 메서드
    public static <T> CustomPage<T> from(Page<T> page) {
        return new CustomPage<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    // CustomPage를 Page로 변환하는 메서드
    public Page<T> toPage() {
        return new PageImpl<>(content, PageRequest.of(number, size), totalElements);
    }
}
