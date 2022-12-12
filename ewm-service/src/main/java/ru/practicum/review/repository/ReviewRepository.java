package ru.practicum.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Event;
import ru.practicum.review.model.Review;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {

    List<Review> findAllByReviewer(User reviewer);

    Optional<Review> findByEventAndReviewer(Event event, User reviewer);

}
