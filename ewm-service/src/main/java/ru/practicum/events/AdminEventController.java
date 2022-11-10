package ru.practicum.events;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path="/admin/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminEventController {

    @Autowired
    EventsService eventsService;

    @GetMapping
    List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                 @RequestParam(required = false) List<String> states,
                                 @RequestParam(required = false) List<Integer> categories,
                                 @RequestParam(required = false) LocalDateTime rangeStart,
                                 @RequestParam(required = false) LocalDateTime rangeEnd,
                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventsService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping(path = "/{eventId}")
    EventFullDto putEvent(@PathVariable long eventId, @RequestBody @Valid NewEventDto newEventDto) {
        return eventsService.putEvent(eventId, newEventDto);
    }

    @PatchMapping(path = "/{eventId}/publish")
    EventFullDto patchPublishEvent(@PathVariable long eventId) {
        return eventsService.publishEvent(eventId);
    }

    @PatchMapping(path = "/{eventId}/reject")
    EventFullDto patchRejectEvent(@PathVariable long eventId) {
        return eventsService.rejectEvent(eventId);
    }
}
