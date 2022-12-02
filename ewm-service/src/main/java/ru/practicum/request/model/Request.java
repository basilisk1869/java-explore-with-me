package ru.practicum.request.model;

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
 * Запрос на участие в событии
 */
@Entity
@Table(name = "requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    /**
     * Идентификатор
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата и время создания
     */
    @CreationTimestamp
    private LocalDateTime created;

    /**
     * Событие
     */
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    /**
     * Пользователь
     */
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    /**
     * Статус запроса
     */
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

}
