package ru.practicum.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Отзыв на событие
 */
@Entity
@Table(name = "reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь, оставивший отзыв
     */
    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    /**
     * Событие, по которому оставлен отзыв
     */
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    /**
     * Дата и время, когда оставлен отзыв
     */
    @CreationTimestamp
    private LocalDateTime created;

    /**
     * Выставленный рейтинг от 0 до 10
     * Отрицательный - от 0 до 4, нейтральный - 5, положительный - от 6 до 10
     */
    private Integer rating;

    /**
     * Текст отзыва
     * Если null, то модерации не требует
     */
    private String text;

    /**
     * Статус обзора
     */
    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.PENDING;

}
