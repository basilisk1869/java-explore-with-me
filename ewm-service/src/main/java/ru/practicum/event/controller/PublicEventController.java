package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.avro.EndpointHitAvro;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.PublicEventService;
import ru.practicum.stats.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {

    @Autowired
    private final PublicEventService publicEventService;

    @Autowired
    private final StatsClient statsClient;

    @GetMapping
    List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                  @RequestParam(required = false) List<Long> categories,
                                  @RequestParam(required = false) Boolean paid,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                  @RequestParam(required = false) Boolean onlyAvailable,
                                  @RequestParam(required = false) String sort,
                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                  @RequestParam(required = false, defaultValue = "10") Integer size,
                                  HttpServletRequest request) {
        log.info("getEvents");
        List<EventShortDto> result = publicEventService.getEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        statsClient.postEndpointHit(EndpointHitAvro.newBuilder()
                .setApp("ewm")
                .setUri(request.getRequestURI())
                .setIp(request.getRemoteAddr())
                .setTimestamp(Instant.now().toEpochMilli())
                .build());
        return result;
    }

    @GetMapping("/{eventId}")
    EventFullDto getEvent(@PathVariable long eventId, HttpServletRequest request) {
        log.info("getEvent " + eventId);
        EventFullDto eventFullDto = publicEventService.getEvent(eventId);
        statsClient.postEndpointHit(EndpointHitAvro.newBuilder()
                .setApp("ewm")
                .setUri(request.getRequestURI())
                .setIp(request.getRemoteAddr())
                .setTimestamp(Instant.now().toEpochMilli())
                .build());
        return eventFullDto;
    }

}
