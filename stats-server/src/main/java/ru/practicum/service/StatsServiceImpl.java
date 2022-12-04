package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StatsServiceImpl implements StatsService {

    @Autowired
    private final EndpointHitRepository endpointHitRepository;

    @Autowired
    private final ModelMapper modelMapper;

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

}
