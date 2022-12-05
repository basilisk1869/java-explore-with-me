package ru.practicum.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewReviewDto {

    @NotNull
    private Long event;

    @NotNull
    @Range(min = 0, max = 10)
    private Integer rating;

    @Length(max = 2000)
    private String text;

}
