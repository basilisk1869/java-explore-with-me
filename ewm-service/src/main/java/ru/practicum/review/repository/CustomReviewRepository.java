package ru.practicum.review.repository;

import io.micrometer.core.lang.Nullable;
import ru.practicum.review.dto.ReviewDto;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.OptionalDouble;

public interface CustomReviewRepository {

    /**
     * Получение отзывов по заданным условиям
     * @param eventId Идентификатор события
     * @param positive Только положительные или отрицательные, null - все
     * @param text Текст поиска в имени автора или тексте отзыва
     * @param from Смещение выборки
     * @param size Размер выборки
     * @return Список отзывов
     */
    @NotNull List<ReviewDto> getReviews(long eventId,
                                        @Nullable Boolean positive,
                                        @Nullable String text,
                                        int from,
                                        int size);

    /**
     * Получение рейтинга события
     * @param eventId Идентификатор события
     * @return Рейтинг события, если он может быть рассчитан
     */
    OptionalDouble getEventRating(long eventId);

    /**
     * Получение рейтинга инициатора
     * @param userId Идентификатор инициатора событий
     * @return Рейтинг инициатора, если он может быть рассчитан
     */
    OptionalDouble getInitiatorRating(long userId);

}
