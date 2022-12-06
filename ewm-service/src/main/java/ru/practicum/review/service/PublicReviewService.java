package ru.practicum.review.service;

import io.micrometer.core.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.practicum.review.dto.ReviewDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public interface PublicReviewService {

    /**
     * Получение отзывов по заданным условиям
     * @param eventId Идентификатор события
     * @param positive Только положительные или отрицательные, null - все
     * @param text Текст поииска в имени автора или тексте отзыва
     * @param from Смещение выборки
     * @param size Размер выборки
     * @return Список отзывов
     */
    @NotNull List<ReviewDto> getReviews(long eventId,
                                        @Nullable Boolean positive,
                                        @Nullable String text,
                                        int from,
                                        int size);

}
