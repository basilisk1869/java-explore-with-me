package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.category.model.Category;
import ru.practicum.location.model.Location;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Событие
 */
@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    /**
     * Идентификатор
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Краткое описание события
     */
    private String annotation;

    /**
     * Категория к которой относится событие
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * Дата и время создания
     */
    @CreationTimestamp
    private LocalDateTime createdOn;

    /**
     * Полное описание события
     */
    private String description;

    /**
     * Дата и время на которые намечено событие
     */
    private LocalDateTime eventDate;

    /**
     * Пользователь инициировавший событие
     */
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    /**
     * Место проведения события
     */
    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;

    /**
     * Нужно ли оплачивать участие в событии
     */
    private Boolean paid = false;

    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     */
    private Integer participantLimit = 0;

    /**
     * Дата и времени опубликования события
     */
    private LocalDateTime publishedOn;

    /**
     * Нужна ли пре-модерация заявок на участие.
     * Если true, то все заявки будут ожидать подтверждения инициатором события.
     * Если false - то будут подтверждаться автоматически.
     */
    private Boolean requestModeration;

    /**
     * Состояние публикации события
     */
    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;

    /**
     * Заголовок события
     */
    private String title;

    /**
     * Подборки, куда входит данное событие
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_compilations",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "compilation_id"))
    private List<Event> compilations;

    /**
     * Запросы на участие в событии
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private List<Request> requests;

}
