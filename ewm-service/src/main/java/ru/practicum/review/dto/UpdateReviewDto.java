package ru.practicum.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Event;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReviewDto {

    private Event event;

    private Integer rating;

    private String text;

}
