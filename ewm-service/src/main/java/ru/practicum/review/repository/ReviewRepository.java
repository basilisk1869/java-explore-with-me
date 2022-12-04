package ru.practicum.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.review.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
}
