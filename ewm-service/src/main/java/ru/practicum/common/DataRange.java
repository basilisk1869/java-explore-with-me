package ru.practicum.common;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataRange<T> {

    int from;

    int size;

    Sort sort;

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
