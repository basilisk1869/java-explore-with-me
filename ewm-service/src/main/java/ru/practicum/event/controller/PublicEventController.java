package ru.practicum.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.service.PublicEventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path="/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicEventController {

    @Autowired
    PublicEventService publicEventService;

    @GetMapping
    List<EventFullDto> getEvents(@RequestParam(required = false) String text,
                                 @RequestParam(required = false) List<Integer> categories,
                                 @RequestParam(required = false) Boolean paid,
                                 @RequestParam(required = false) LocalDateTime rangeStart,
                                 @RequestParam(required = false) LocalDateTime rangeEnd,
                                 @RequestParam(required = false) Boolean onlyAvailable,
                                 @RequestParam(required = false) String sort,
                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        return publicEventService.getEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    EventFullDto getEvent(@PathVariable long eventId) {
        return publicEventService.getEvent(eventId);
    }

}
