package ru.practicum.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import ru.practicum.dto.ViewStats;
import ru.practicum.model.QEndpointHit;

@RequiredArgsConstructor
public class CustomEndpointHitRepositoryImpl implements CustomEndpointHitRepository {

    private final EntityManager entityManager;

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
