package ru.practicum.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsController {

    @Autowired
    StatsService statsService;

    @PostMapping(path = "/hit")
    void postEndpointHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        statsService.postEndpointHit(endpointHitDto);
    }

    @GetMapping(path = "stats")
    List<ViewStats> getViewStats(@RequestParam LocalDateTime start,
                                 @RequestParam LocalDateTime end,
                                 @RequestParam(required = false) List<String> uris,
                                 @RequestParam(required = false) Boolean unique) {
        return statsService.getViewStats(start, end, uris, unique);
    }

}
