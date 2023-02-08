package ru.practicum.review.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.core.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.model.QReview;
import ru.practicum.review.model.Review;
import ru.practicum.review.model.ReviewStatus;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {

    private final EntityManager entityManager;

    private final ModelMapper modelMapper;

    private static final int NEUTRAL_RATING = 5;

    @Override
    public @NotNull List<ReviewDto> getReviews(long eventId,
                                               @Nullable Boolean positive,
                                               @Nullable String text,
                                               int from,
                                               int size) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QReview qReview = QReview.review;
        JPAQuery<Review> jpaQuery = jpaQueryFactory.select(qReview)
                .from(qReview)
                .where(qReview.event.id.eq(eventId))
                .where(qReview.status.eq(ReviewStatus.CONFIRMED));
        // positive
        if (positive != null) {
            if (positive) {
                jpaQuery.where(qReview.rating.gt(NEUTRAL_RATING));
            } else {
                jpaQuery.where(qReview.rating.lt(NEUTRAL_RATING));
            }
        }
        // text
        if (text != null) {
            jpaQuery.where(qReview.reviewer.name.containsIgnoreCase(text)
                    .or(qReview.text.containsIgnoreCase(text)));
        }
        // from
        jpaQuery.offset(from);
        // size
        jpaQuery.limit(size);
        // mapping
        return jpaQuery.fetch().stream()
                .map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public OptionalDouble getEventRating(long eventId) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QReview qReview = QReview.review;
        List<Integer> ratingList = jpaQueryFactory.select(qReview.rating)
                .from(qReview)
                .where(qReview.event.id.eq(eventId))
                .fetch();
        return ratingList.stream()
                .mapToDouble(Double::valueOf)
                .average();
    }

    @Override
    public OptionalDouble getInitiatorRating(long userId) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QReview qReview = QReview.review;
        List<Integer> ratingList = jpaQueryFactory.select(qReview.rating)
                .from(qReview)
                .where(qReview.event.initiator.id.eq(userId))
                .fetch();
        return ratingList.stream()
                .mapToDouble(Double::valueOf)
                .average();
    }

}
