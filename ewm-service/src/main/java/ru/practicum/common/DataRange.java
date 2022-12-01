package ru.practicum.common;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@RequiredArgsConstructor
public class DataRange<T> {

    private final int from;

    private final int size;

    private final Sort sort;

    public boolean isValid() {
        if (from < 0) {
            return false;
        }
        if (size <= 0) {
            return false;
        }
        return true;
    }

    public Pageable getPageable() {
        if (from % size != 0) {
            return PageRequest.of(from / size, size * 2, sort);
        } else {
            return PageRequest.of(from / size, size, sort);
        }
    }

    public List<T> trimPage(List<T> page) {
        if (from % size != 0) {
            return page.subList(from % size, (from % size) + size);
        } else {
            return page;
        }
    }

}
