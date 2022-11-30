package ru.practicum.repository;

import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomEndpointHitRepository {

    List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
