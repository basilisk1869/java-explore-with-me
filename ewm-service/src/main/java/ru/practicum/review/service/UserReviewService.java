package ru.practicum.review.service;

import org.springframework.stereotype.Service;
import ru.practicum.review.dto.NewReviewDto;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.dto.UpdateReviewDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface UserReviewService {

    /**
     * Получение всех отзывов пользователя
     * @param userId Идентификатор пользователя
     * @return Список отзывов
     */
    @NotNull List<ReviewDto> getReviews(long userId);

    /**
     * Добавление отзыва
     * @param userId Идентификатор пользователя
     * @param newReviewDto Отзыв
     * @return Данные отзыва
     */
    @NotNull ReviewDto postReview(long userId, @NotNull NewReviewDto newReviewDto);

    /**
     * Изменение отзыва
     * @param userId Идентификатор пользователя
     * @param reviewId Идентификатор отзыва
     * @param updateReviewDto Обновленные данные
     * @return Обновленные данные отзыва
     */
    @NotNull ReviewDto patchReview(long userId, long reviewId, @NotNull UpdateReviewDto updateReviewDto);

    /**
     * Получение отзыва пользователем
     * @param userId Идентификатор пользователя
     * @param reviewId Идентификатор отзыва
     * @return Данные отзыва
     */
    @NotNull ReviewDto getReview(long userId, long reviewId);

    /**
     * Удаление отзыва
     * @param userId Идентификатор пользователя
     * @param reviewId Идентификатор отзыва
     */
    void deleteReview(long userId, long reviewId);

    /**
     * Проверка, что дата события в прошлом (для тестирования)
     * @param eventDate Дата события
     * @return Если событие завершилось - true, иначе - false
     */
    boolean checkEventIsEnded(@NotNull LocalDateTime eventDate);

}
