package ru.practicum.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.AdminEventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path="/admin/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminEventController {

    @Autowired
    AdminEventService adminEventService;

    @GetMapping
    List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                 @RequestParam(required = false) List<String> states,
                                 @RequestParam(required = false) List<Integer> categories,
                                 @RequestParam(required = false) LocalDateTime rangeStart,
                                 @RequestParam(required = false) LocalDateTime rangeEnd,
                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        return adminEventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping(path = "/{eventId}")
    EventFullDto putEvent(@PathVariable long eventId, @RequestBody @Valid NewEventDto newEventDto) {
        return adminEventService.putEvent(eventId, newEventDto);
    }

    @PatchMapping(path = "/{eventId}/publish")
    EventFullDto patchPublishEvent(@PathVariable long eventId) {
        return adminEventService.publishEvent(eventId);
    }

    @PatchMapping(path = "/{eventId}/reject")
    EventFullDto patchRejectEvent(@PathVariable long eventId) {
        return adminEventService.rejectEvent(eventId);
    }
}
