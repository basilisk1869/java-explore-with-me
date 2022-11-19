package ru.practicum.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.service.PublicEventService;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
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
                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                 @RequestParam(required = false) Boolean onlyAvailable,
                                 @RequestParam(required = false) String sort,
                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                 @RequestParam(required = false, defaultValue = "10") Integer size,
                                 HttpServletRequest request) {
        log.info("getEvents");
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
        log.info("getEvent " + eventId);
        EventFullDto eventFullDto = publicEventService.getEvent(eventId);
        statsClient.postEndpointHit(EndpointHitDto.builder()
                .app("ewm")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .build());
        return eventFullDto;
    }

}
