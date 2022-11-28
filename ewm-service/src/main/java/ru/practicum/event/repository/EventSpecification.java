package ru.practicum.event.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

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
        final String preparedText;
        if (text != null) {
            preparedText = "%" + text.toUpperCase() + "%";
        } else {
            preparedText = null;
        }
        return (root, query, builder) -> builder.and(
            preparedText == null ? builder.isTrue(builder.literal(true)) :
                builder.or(
                    builder.like(builder.upper(root.get("annotation")), preparedText),
                    builder.like(builder.upper(root.get("description")), preparedText)),
            builder.and(
                categories.size() == 0 ? builder.isTrue(builder.literal(true))
                        : root.<Category>get("category").in(categories),
                builder.and(
                    paid == null ? builder.isTrue(builder.literal(true)) : builder.equal(root.get("paid"), paid),
                    rangeStart == null && rangeEnd == null ?
                            builder.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.now())
                            : builder.and(
                                rangeStart == null ? builder.isTrue(builder.literal(true)) : builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart),
                                rangeEnd == null ?  builder.isTrue(builder.literal(true)) : builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd)))));
    }
}
