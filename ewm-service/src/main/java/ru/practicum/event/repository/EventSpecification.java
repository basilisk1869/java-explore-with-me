package ru.practicum.event.repository;

import java.beans.Expression;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.user.model.User;

public class EventSpecification {

    public static Specification<Event> getEventsByAdmin(List<User> users, List<EventState> states,
                                                       List<Category> categories, LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd) {
        return (root, query, builder) -> builder.and(
            users.size() == 0 ? builder.isTrue(builder.literal(true)) : root.get("initiator").in(users),
            builder.and(
                states.size() == 0 ?  builder.isTrue(builder.literal(true)) : root.get("state").in(states),
                builder.and(
                    categories.size() == 0 ? builder.isTrue(builder.literal(true)) : root.get("category").in(categories),
                    builder.and(
                        rangeStart == null ? builder.isTrue(builder.literal(true)) : builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart),
                        rangeEnd == null ?  builder.isTrue(builder.literal(true)) : builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd)))));
    }

    public static Specification<Event> getEventsByPublic(String text, List<Category> categories,
        Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable) {
        return (root, query, builder) -> builder.and(
            text == null ?  builder.isTrue(builder.literal(true)) :
                builder.or(
                    builder.like(builder.upper(root.get("annotation")), "%" + text.toUpperCase() + "%"),
                    builder.like(builder.upper(root.get("description")), "%" + text.toUpperCase() + "%")),
            builder.and(
                categories.size() == 0 ?  builder.isTrue(builder.literal(true)) : root.get("category").in(categories),
                builder.and(
                    paid == null ?  builder.isTrue(builder.literal(true)) : builder.isTrue(root.get("paid")),
                    rangeStart == null && rangeEnd == null ?
                        builder.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.now()) :
                        builder.and(
                            rangeStart == null ? builder.isTrue(builder.literal(true)) : builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart),
                            rangeEnd == null ?  builder.isTrue(builder.literal(true)) : builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd)))));
    }
}
