package ru.practicum.service;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.QEndpointHit;
import ru.practicum.repository.EndpointHitRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    EndpointHitRepository endpointHitRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EntityManager entityManager;

    @Override
    public void postEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = modelMapper.map(endpointHitDto, EndpointHit.class);
        endpointHitRepository.save(endpointHit);
    }

    @Override
    public List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QEndpointHit qEndpointHit = QEndpointHit.endpointHit;
        NumberExpression<Long> rowCount;
        if (unique == null || !unique) {
            rowCount = qEndpointHit.timestamp.count();
        } else {
            rowCount = qEndpointHit.ip.countDistinct();
        }
        JPAQuery<ViewStats> jpaQuery = jpaQueryFactory
                .select(Projections.constructor(ViewStats.class, qEndpointHit.app, qEndpointHit.uri, rowCount))
                .from(qEndpointHit)
                .where(qEndpointHit.timestamp.between(start, end));
        if (uris.size() > 0) {
            jpaQuery.where(qEndpointHit.uri.in(uris));
        }
        jpaQuery.groupBy(qEndpointHit.app, qEndpointHit.uri);
        return jpaQuery.fetch();
    }

}
