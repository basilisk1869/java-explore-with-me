package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
public class StatsController {

    @Autowired
    private final StatsService statsService;

    @PostMapping(path = "/hit")
    void postEndpointHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("post hit : " + endpointHitDto.toString());
        statsService.postEndpointHit(endpointHitDto);
    }

    @GetMapping(path = "/stats")
    List<ViewStats> getViewStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                 @RequestParam(required = false) List<String> uris,
                                 @RequestParam(required = false) Boolean unique) {
        log.info("get stats : " + start + " " + end + " " + uris + " " + unique);
        return statsService.getViewStats(start, end, uris, unique);
    }

}
