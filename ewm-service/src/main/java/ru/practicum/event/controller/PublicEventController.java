package ru.practicum.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.service.PublicEventService;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path="/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicEventController {

    @Autowired
    PublicEventService publicEventService;

    @Autowired
    StatsClient statsClient;

    @GetMapping
    List<EventFullDto> getEvents(@RequestParam(required = false) String text,
                                 @RequestParam(required = false) List<Integer> categories,
                                 @RequestParam(required = false) Boolean paid,
                                 @RequestParam(required = false) LocalDateTime rangeStart,
                                 @RequestParam(required = false) LocalDateTime rangeEnd,
                                 @RequestParam(required = false) Boolean onlyAvailable,
                                 @RequestParam(required = false) String sort,
                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                 @RequestParam(required = false, defaultValue = "10") Integer size,
                                 HttpServletRequest request) {
        List<EventFullDto> result = publicEventService.getEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        statsClient.postEndpointHit(EndpointHitDto.builder()
                .app("ewm")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .build());
        return result;
    }

    @GetMapping("/{eventId}")
    EventFullDto getEvent(@PathVariable long eventId, HttpServletRequest request) {
        EventFullDto eventFullDto = publicEventService.getEvent(eventId);
        statsClient.postEndpointHit(EndpointHitDto.builder()
                .app("ewm")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .build());
        return eventFullDto;
    }

}
