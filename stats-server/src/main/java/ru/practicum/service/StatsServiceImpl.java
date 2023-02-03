package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.practicum.avro.EndpointHitAvro;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.EndpointHitRepository;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final EndpointHitRepository endpointHitRepository;

    private final ModelMapper modelMapper;

    public StatsServiceImpl(@Autowired  EndpointHitRepository endpointHitRepository,
                            @Autowired ModelMapper modelMapper) {
        this.endpointHitRepository = endpointHitRepository;
        this.modelMapper = modelMapper;
    }

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

    @KafkaListener(topics = "hits", containerFactory = "endpointHitKafkaListenerContainerFactory")
    private void consume(EndpointHitAvro endpointHitAvro) {
        log.info("listenHits: hit - " + endpointHitAvro.toString());
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(String.valueOf(endpointHitAvro.getApp()))
                .uri(String.valueOf(endpointHitAvro.getUri()))
                .ip(String.valueOf(endpointHitAvro.getIp()))
                .timestamp(Instant.ofEpochMilli(endpointHitAvro.getTimestamp())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .build();
        postEndpointHit(endpointHitDto);
    }

}
