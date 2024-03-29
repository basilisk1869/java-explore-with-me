package ru.practicum.event.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.lang.Nullable;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.QEvent;
import ru.practicum.request.model.QRequest;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.review.model.QReview;
import ru.practicum.user.model.QUser;
import ru.practicum.user.model.User;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.event.model.EventSort.EVENT_DATE;

@RequiredArgsConstructor
public class CustomEventRepositoryImpl implements CustomEventRepository {

    private final EntityManager entityManager;

    private final ModelMapper modelMapper;

    @Override
    public @NotNull List<EventFullDto> getEvents(@Nullable List<Long> userIds,
                                                 @Nullable List<String> stateIds,
                                                 @Nullable List<Long> categoryIds,
                                                 @Nullable LocalDateTime rangeStart,
                                                 @Nullable LocalDateTime rangeEnd,
                                                 int from,
                                                 int size) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QEvent qEvent = QEvent.event;
        QRequest qRequest = QRequest.request;
        JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(qEvent, qRequest.id.count())
                .from(qEvent)
                .leftJoin(qRequest)
                .on(qRequest.event.eq(qEvent).and(qRequest.status.eq(RequestStatus.CONFIRMED)));
        // userIds
        if (userIds != null) {
            jpaQuery.where(qEvent.initiator.id.in(userIds));
        }
        // stateIds
        if (stateIds != null) {
            List<EventState> states = stateIds.stream()
                    .map(EventState::valueOf)
                    .collect(Collectors.toList());
            jpaQuery.where(qEvent.state.in(states));
        }
        // categoryIds
        if (categoryIds != null) {
            jpaQuery.where(qEvent.category.id.in(categoryIds));
        }
        // rangeStart
        if (rangeStart != null) {
            jpaQuery.where(qEvent.eventDate.goe(rangeStart));
        }
        // rangeEnd
        if (rangeEnd != null) {
            jpaQuery.where(qEvent.eventDate.loe(rangeEnd));
        }
        // group by
        jpaQuery.groupBy(qEvent.id);
        // from
        jpaQuery.offset(from);
        // size
        jpaQuery.limit(size);
        // mapping
        return getEventFullDtoFromQuery(jpaQuery, qEvent, qRequest);
    }

    @Override
    public @NotNull List<EventFullDto> getEvents(@NotNull User initiator,
                                                 int from,
                                                 int size) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QEvent qEvent = QEvent.event;
        QRequest qRequest = QRequest.request;
        JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(qEvent, qRequest.id.count())
                .from(qEvent)
                .leftJoin(qRequest)
                .on(qRequest.event.eq(qEvent).and(qRequest.status.eq(RequestStatus.CONFIRMED)))
                .where(qEvent.initiator.eq(initiator))
                .groupBy(qEvent.id)
                .orderBy(qEvent.id.asc())
                .offset(from)
                .limit(size);
        return getEventFullDtoFromQuery(jpaQuery, qEvent, qRequest);
    }

    @Override
    public @NotNull List<EventShortDto> getEvents(@Nullable String text,
                                           @Nullable List<Long> categoryIds,
                                           @Nullable Boolean paid,
                                           @Nullable LocalDateTime rangeStart,
                                           @Nullable LocalDateTime rangeEnd,
                                           @Nullable Boolean onlyAvailable,
                                           @Nullable String sort,
                                           int from,
                                           int size) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QEvent qEvent = QEvent.event;
        QRequest qRequest = QRequest.request;
        JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(qEvent, qRequest.id.count())
                .from(qEvent)
                .leftJoin(qRequest)
                .on(qRequest.event.eq(qEvent).and(qRequest.status.eq(RequestStatus.CONFIRMED)))
                .where(qEvent.state.eq(EventState.PUBLISHED));
        // text
        if (text != null) {
            String preparedText = "%" + text.toUpperCase() + "%";
            jpaQuery.where(qEvent.annotation.upper().like(preparedText)
                    .or(qEvent.description.upper().like(preparedText)));
        }
        // categoryIds
        if (categoryIds != null) {
            jpaQuery.where(qEvent.category.id.in(categoryIds));
        }
        // paid
        if (paid != null) {
            jpaQuery.where(qEvent.paid.eq(paid));
        }
        // rangeStart & rangeEnd
        if (rangeStart == null && rangeEnd == null) {
            jpaQuery.where(qEvent.eventDate.goe(LocalDateTime.now()));
        } else {
            if (rangeStart != null) {
                jpaQuery.where(qEvent.eventDate.goe(rangeStart));
            }
            if (rangeEnd != null) {
                jpaQuery.where(qEvent.eventDate.loe(rangeEnd));
            }
        }
        // group by
        jpaQuery.groupBy(qEvent.id);
        // onlyAvailable
        if (onlyAvailable != null && onlyAvailable) {
            jpaQuery.where(qRequest.id.count().lt(qEvent.participantLimit));
        }
        // sort by event date
        if (sort != null) {
            EventSort eventSort = EventSort.valueOf(sort);
            if (Objects.equals(eventSort, EVENT_DATE)) {
                jpaQuery.orderBy(qEvent.eventDate.asc());
                // from
                jpaQuery.offset(from);
                // size
                jpaQuery.limit(size);
            }
        } else {
            // from
            jpaQuery.offset(from);
            // size
            jpaQuery.limit(size);
        }
        // mapping
        return getEventShortDtoFromQuery(jpaQuery, qEvent, qRequest);
    }

    /**
     * Получение полных DTO событий из запроса
     * @param jpaQuery Запрос
     * @param qEvent Сущность события
     * @param qRequest Сущность запроса
     * @return Список полных DTO событий
     */
    private List<EventFullDto> getEventFullDtoFromQuery(JPAQuery<Tuple> jpaQuery, QEvent qEvent, QRequest qRequest) {
        List<EventFullDto> events = jpaQuery.fetch().stream()
                .map(tuple -> {
                    EventFullDto event = modelMapper.map(tuple.get(qEvent), EventFullDto.class);
                    event.setConfirmedRequests(tuple.get(qRequest.id.count()));
                    if (event.getConfirmedRequests() == null) {
                        event.setConfirmedRequests(0L);
                    }
                    return event;
                })
                .collect(Collectors.toList());
        Map<Long, Double> ratings = getRatings(events.stream()
                .map(EventFullDto::getId)
                .collect(Collectors.toList()));
        events.forEach(event -> event.setRating(ratings.getOrDefault(event.getId(), null)));
        return events;
    }

    /**
     * Получение коротких DTO событий из запроса
     * @param jpaQuery Запрос
     * @param qEvent Сущность события
     * @param qRequest Сущность запроса
     * @return Список коротких DTO событий
     */
    private List<EventShortDto> getEventShortDtoFromQuery(JPAQuery<Tuple> jpaQuery, QEvent qEvent, QRequest qRequest) {
        List<EventShortDto> events = jpaQuery.fetch().stream()
                .map(tuple -> {
                    EventShortDto event = modelMapper.map(tuple.get(qEvent), EventShortDto.class);
                    event.setConfirmedRequests(tuple.get(qRequest.id.count()));
                    if (event.getConfirmedRequests() == null) {
                        event.setConfirmedRequests(0L);
                    }
                    return event;
                })
                .collect(Collectors.toList());
        Map<Long, Double> ratings = getRatings(events.stream()
                .map(EventShortDto::getId)
                .collect(Collectors.toList()));
        events.forEach(event -> event.setRating(ratings.getOrDefault(event.getId(), null)));
        return events;
    }

    /**
     * Получение рейтингов событий
     * @param eventIds Список идентификаторов событий
     * @return Список пар идентификаторов событий и их рейтинга
     */
    private Map<Long, Double> getRatings(List<Long> eventIds) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QReview qReview = QReview.review;
        QEvent qEvent = QEvent.event;
        QUser qUser = QUser.user;
        List<Tuple> jpaQuery = jpaQueryFactory.select(qEvent.id, qReview.rating.avg())
                .from(qReview)
                .leftJoin(qEvent)
                .on(qReview.event.eq(qEvent))
                .leftJoin(qUser)
                .on(qEvent.initiator.eq(qUser))
                .where(qReview.event.id.in(eventIds))
                .where(qUser.showRating.eq(true))
                .groupBy(qEvent.id)
                .fetch();
        return jpaQuery.stream()
                .collect(Collectors.toMap(
                        t -> t.get(qEvent.id),
                        t -> t.get(qReview.rating.avg())));
    }

}
