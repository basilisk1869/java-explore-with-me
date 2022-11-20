package ru.practicum.request.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequester(User requester);

    Optional<Request> findByRequesterAndEvent(User requester, Event event);

}
