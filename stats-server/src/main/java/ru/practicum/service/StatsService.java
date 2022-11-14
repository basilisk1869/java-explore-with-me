package ru.practicum.service;

import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.EndpointHitDto;

import javax.validation.Valid;

public interface StatsService {

    void postEndpointHit(EndpointHitDto endpointHitDto);

}
