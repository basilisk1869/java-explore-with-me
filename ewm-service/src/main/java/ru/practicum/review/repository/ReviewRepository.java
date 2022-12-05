package ru.practicum.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.review.model.Review;
import ru.practicum.user.model.User;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {

    List<Review> findAllByReviewer(User reviewer);

}
