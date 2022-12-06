package ru.practicum.review.service;

import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.model.ReviewStatus;

import javax.validation.constraints.NotNull;

public interface AdminReviewService {

    /**
     * Модерация отзыва
     * @param reviewId Идентификатор отзыва
     * @param reviewStatus Задаваемый статус
     * @return Обновленный отзыв
     */
    @NotNull ReviewDto setReviewStatus(long reviewId, @NotNull ReviewStatus reviewStatus);

}
