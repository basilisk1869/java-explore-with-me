package ru.practicum.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.EndpointHitRepository;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    EndpointHitRepository endpointHitRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void postEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = modelMapper.map(endpointHitDto, EndpointHit.class);
        endpointHitRepository.save(endpointHit);
    }

}
