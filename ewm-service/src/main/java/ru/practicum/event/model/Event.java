package ru.practicum.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.model.Category;
import ru.practicum.event.Location;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ru.practicum.events")
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

}
