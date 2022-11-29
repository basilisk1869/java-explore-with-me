package ru.practicum.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.category.model.Category;
import ru.practicum.location.model.Location;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @CreationTimestamp
    LocalDateTime createdOn;

    String description;

    LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;

    @OneToOne
    @JoinColumn(name = "location_id")
    Location location;

    Boolean paid = false;

    Integer participantLimit = 0;

    LocalDateTime publishedOn;

    Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    EventState state = EventState.PENDING;

    String title;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_compilations",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "compilation_id"))
    List<Event> compilations;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    List<Request> requests;
}
