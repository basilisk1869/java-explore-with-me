package ru.practicum.event.repository;

import java.beans.Expression;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import ru.practicum.event.model.EventState;
import ru.practicum.user.model.User;

public class EventSpecification {

    public static Specification<Event> getEventByAdmin(List<User> users, List<EventState> states,
                                                       List<Category> categories, LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd) {
        return (root, query, builder) -> builder.and(
            users.size() == 0 ? builder.literal(true) : root.get("initiator").in(users),
            builder.and(
                states.size() == 0 ? builder.literal(true) : root.get("state").in(states),
                builder.and(
                    categories.size() == 0 ? builder.literal(true) : root.get("category").in(categories),
                    builder.and(
                        builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart),
                        builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd)))));
    }
}
