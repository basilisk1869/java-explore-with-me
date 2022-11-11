package ru.practicum.event.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public class EventSpecification {

    public static Specification<Event> getEventByAdmin(List<Long> users, List<String> states,
                                                       List<Long> categories, LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd) {
        return (root, query, builder) -> builder.and(
                builder.isTrue(builder.literal(users.size() == 0 || users.contains(root.get("initiator_id")))),
                builder.and(
                        builder.isTrue(builder.literal(states.size() == 0 || states.contains(root.get("state")))),
                        builder.and(
                                builder.isTrue(builder.literal(categories.size() == 0 || categories.contains(root.get("category_id")))),
                                builder.and(
                                        builder.greaterThanOrEqualTo(root.<LocalDateTime>get("event_date"), rangeStart),
                                        builder.lessThanOrEqualTo(root.<LocalDateTime>get("end"), rangeEnd)))));
    }

}
