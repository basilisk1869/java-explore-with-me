package ru.practicum.service;

import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void postEndpointHit(EndpointHitDto endpointHitDto);

    List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
