package ru.practicum.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

@Component
public class GetterRepository {

  @Autowired
  UserRepository userRepository;

  @Autowired
  EventRepository eventRepository;

  @Autowired
  CompilationRepository compilationRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  LocationRepository locationRepository;

  @Autowired
  RequestRepository requestRepository;

  public Category getCategory(long catId) {
    return categoryRepository.findById(catId).orElseThrow(() -> {
      throw new NotFoundException("category is not found");
    });
  }

  public Compilation getCompilation(long compId) {
    return compilationRepository.findById(compId).orElseThrow(() -> {
      throw new NotFoundException("compilation is not found");
    });
  }

  public Event getEvent(long eventId) {
    return eventRepository.findById(eventId).orElseThrow(() -> {
      throw new NotFoundException("event is not found");
    });
  }

  public Event getEventByUser(long userId, long eventId) {
    User initiator = getUser(userId);
    Event event = getEvent(eventId);
    if (event.getInitiator().equals(initiator)) {
      return event;
    } else {
      throw new AccessDeniedException("user has no access for this event");
    }
  }

  public User getUser(long catId) {
    return userRepository.findById(catId).orElseThrow(() -> {
      throw new NotFoundException("user is not found");
    });
  }

  public Request getRequest(long reqId) {
    return requestRepository.findById(reqId).orElseThrow(() -> {
      throw new NotFoundException("request is not found");
    });
  }
}
