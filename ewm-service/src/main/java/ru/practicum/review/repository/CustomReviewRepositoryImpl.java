package ru.practicum.review.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import ru.practicum.event.model.Event;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.model.QReview;
import ru.practicum.review.model.Review;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {

    private final EntityManager entityManager;

    private final ModelMapper modelMapper;

    @Override
    public List<ReviewDto> getReviews(Event event, Boolean positive, String text, int from, int size) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QReview qReview = QReview.review;
        JPAQuery<Review> jpaQuery = jpaQueryFactory.select(qReview)
                .from(qReview);
        // positive
        if (positive != null) {
            if (positive) {
                jpaQuery.where(qReview.rating.gt(0));
            } else {
                jpaQuery.where(qReview.rating.lt(0));
            }
        }
        // text
        if (text != null) {
            String preparedText = "%" + text.toUpperCase() + "%";
            jpaQuery.where(qReview.text.like(preparedText));
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

}
