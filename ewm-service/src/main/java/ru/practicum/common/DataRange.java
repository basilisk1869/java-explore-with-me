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

    /**
     * Проверяет корректность смещения и размера выборки
     * @return Корректность данных
     */
    public boolean isValid() {
        if (from < 0) {
            return false;
        }
        if (size <= 0) {
            return false;
        }
        return true;
    }

    /**
     * Получение страницы для выборки
     * @return Страницу, гарантированно содержащую заданный диапазон
     */
    public Pageable getPageable() {
        if (from % size != 0) {
            return PageRequest.of(from / size, size * 2, sort);
        } else {
            return PageRequest.of(from / size, size, sort);
        }
    }

    /**
     * Выделение данных по диапазону из полученной страницы
     * @param page Страница данных
     * @return Выделенный диапазон из страницы
     */
    public List<T> trimPage(List<T> page) {
        if (from % size != 0) {
            return page.subList(from % size, (from % size) + size);
        } else {
            return page;
        }
    }

}
