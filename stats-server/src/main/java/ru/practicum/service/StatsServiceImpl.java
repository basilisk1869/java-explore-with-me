package ru.practicum.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.EndpointHitRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    @Autowired
    private final EndpointHitRepository endpointHitRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final ObjectMapper objectMapper;

    @Override
    public void postEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = modelMapper.map(endpointHitDto, EndpointHit.class);
        endpointHitRepository.save(endpointHit);
    }

    @Override
    public @NotNull List<ViewStats> getViewStats(
            @NotNull LocalDateTime start,
            @NotNull LocalDateTime end,
            @Nullable List<String> uris,
            @Nullable Boolean unique) {
        return endpointHitRepository.getViewStats(start, end, uris, unique);
    }

    @KafkaListener(topics = "hits", groupId = "ewm_id")
    private void listenHits(String message) {
        try {
            EndpointHitDto endpointHitDto = objectMapper.readValue(message, EndpointHitDto.class);
            postEndpointHit(endpointHitDto);
        } catch (JsonProcessingException e) {
            log.info("listenHits: JsonProcessingException - " + e);
        }
    }

}
