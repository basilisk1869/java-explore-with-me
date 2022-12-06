package ru.practicum.common.repository;

import ru.practicum.category.model.Category;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.Event;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.model.Request;
import ru.practicum.review.model.Review;
import ru.practicum.user.model.User;

import javax.validation.constraints.NotNull;

public interface CommonRepository {

    /**
     * Получение категории по идентификатору
     * @param catId Идентификатор категории
     * @return Существующую категорию или бросает {@link NotFoundException}
     */
    @NotNull Category getCategory(long catId);

    /**
     * Получение подборки событий по идентификатору
     * @param compId Идентификатор подборки событий
     * @return Существующую подборку событий или бросает {@link NotFoundException}
     */
    @NotNull Compilation getCompilation(long compId);

    /**
     * Получение события по идентификатору
     * @param eventId Идентификатор события
     * @return Существующее событие или бросает {@link NotFoundException}
     */
    @NotNull Event getEvent(long eventId);

    /**
     * Получение события по идентификатору инициатора и идентификатору события
     * @param userId Идентификатор пользователя
     * @param eventId Идентификатор события
     * @return Существующее событие
     *         или бросает {@link NotFoundException}, если пользователь или событие не найдены
     *         или бросает {@link ru.practicum.exception.AccessDeniedException}, если пользователь не является инициатором события
     */
    @NotNull Event getEventByUser(long userId, long eventId);

    /**
     * Получение отзыва
     * @param reviewId Идентификатор отзыва
     * @return Существующий отзыв
     *         или бросает {@link NotFoundException}, если отзыв не найден
     */
    @NotNull Review getReview(long reviewId);

    /**
     * Получение отзыва по идентификатору инициатора и идентификатору события
     * @param userId Идентификатор пользователя
     * @param reviewId Идентификатор отзыва
     * @return Существующий отзыв
     *         или бросает {@link NotFoundException}, если пользователь или отзыв не найдены
     *         или бросает {@link ru.practicum.exception.AccessDeniedException}, если пользователь не является автором отзыва
     */
    @NotNull Review getReviewByUser(long userId, long reviewId);

    /**
     * Получение пользователя по идентификатору
     * @param userId Идентификатор пользователя
     * @return Существующего пользователя или бросает {@link NotFoundException}
     */
    @NotNull User getUser(long userId);

    /**
     * Получение запроса на участие по идентификатору
     * @param reqId Идентификатор запроса на участие
     * @return Существующий запрос на участие или бросает {@link NotFoundException}
     */
    @NotNull Request getRequest(long reqId);

    /**
     * Получение количества подтвержденных запросов на участие в событии
     * @param event Событие
     * @return Количество подтвержденных запросов
     */
    long getConfirmedRequests(@NotNull Event event);

    /**
     * Получение полного DTO по событию
     * @param event Событие
     * @return Полный DTO
     */
    @NotNull EventFullDto mapEventToFullDto(@NotNull Event event);

}
