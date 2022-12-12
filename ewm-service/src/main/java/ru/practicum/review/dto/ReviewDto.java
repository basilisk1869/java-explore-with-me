package ru.practicum.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.review.model.ReviewStatus;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long id;

    private UserShortDto reviewer;

    private EventShortDto event;

    private LocalDateTime created;

    private Integer rating;

    private String text;

    private ReviewStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewDto reviewDto = (ReviewDto) o;
        return Objects.equals(id, reviewDto.id)
                && Objects.equals(reviewer, reviewDto.reviewer)
                && Objects.equals(event, reviewDto.event)
                && Objects.equals(created.truncatedTo(ChronoUnit.MILLIS),
                                  reviewDto.created.truncatedTo(ChronoUnit.MILLIS))
                && Objects.equals(rating, reviewDto.rating)
                && Objects.equals(text, reviewDto.text)
                && status == reviewDto.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reviewer, event, created, rating, text, status);
    }
}
