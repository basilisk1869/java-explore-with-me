package ru.practicum.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.AdminEventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminEventController {

    @Autowired
    AdminEventService adminEventService;

    @GetMapping
    List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                 @RequestParam(required = false) List<String> states,
                                 @RequestParam(required = false) List<Long> categories,
                                 @RequestParam(required = false) LocalDateTime rangeStart,
                                 @RequestParam(required = false) LocalDateTime rangeEnd,
                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("getEvents");
        return adminEventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    EventFullDto putEvent(@PathVariable long eventId, @RequestBody @Valid NewEventDto newEventDto) {
        log.info("putEvent " + eventId + " " + newEventDto);
        return adminEventService.putEvent(eventId, newEventDto);
    }

    @PatchMapping("/{eventId}/publish")
    EventFullDto patchPublishEvent(@PathVariable long eventId) {
        log.info("patchPublishEvent " + eventId);
        return adminEventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    EventFullDto patchRejectEvent(@PathVariable long eventId) {
        log.info("patchRejectEvent " + eventId);
        return adminEventService.rejectEvent(eventId);
    }
}
