package ru.practicum.stats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

/**
 * Клиент запроса статистики просмотров
 */
@Component
@Slf4j
public class StatsClient {

    private final RestTemplate restTemplate;

    /**
     * Создание клиента
     * @param environment Переменные конфигурации
     * @param builder Генератор для RestTemplate
     */
    public StatsClient(@Autowired Environment environment, @Autowired RestTemplateBuilder builder) {
        restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(
                    Objects.requireNonNull(environment.getProperty("stats-server-url"))))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    /**
     * Отправка данных о просмотре
     * @param endpointHitDto Данные о просмотре
     * @return Ответ сервера статистики
     */
    public @NotNull ResponseEntity<Object> postEndpointHit(@NotNull EndpointHitDto endpointHitDto) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(endpointHitDto, defaultHeaders());
        return restTemplate.exchange("/hit", HttpMethod.POST,
                requestEntity, Object.class);
    }

    /**
     * Запрос данных о просмотрах
     * @param start Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris Список uri для которых нужно выгрузить статистику
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip)
     * @return Ответ сервера статистики
     */
    public @NotNull ResponseEntity<List<ViewStats>> getUrlViews(@NotNull LocalDateTime start,
                                                                @NotNull LocalDateTime end,
                                                                @Nullable List<String> uris,
                                                                @Nullable Boolean unique) {
        String path = "/views?start={start}&end={end}";
        Map<String, Object> parameters = new TreeMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        if (uris != null) {
            parameters.put("uris", uris);
            path += "&uris={uris}";
        }
        if (unique != null) {
            parameters.put("unique", unique);
            path += "&unique={unique}";
        }
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, defaultHeaders());
        return restTemplate.exchange(path, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<List<ViewStats>>(){}, parameters);
    }

    /**
     * Добавление данных о просмотрах в полные DTO событий
     * @param events Полные DTO событий
     * @param rangeStart Дата и время начала диапазона за который нужно выгрузить статистику
     * @param rangeEnd Дата и время конца диапазона за который нужно выгрузить статистику
     */
    public void setViewsForEventFullDtoList(
            @NotNull List<EventFullDto> events,
            @NotNull LocalDateTime rangeStart,
            @NotNull LocalDateTime rangeEnd) {
        events.forEach(event -> event.setViews(0L));
        if (events.size() > 0 && rangeStart != null && rangeEnd != null) {
            List<String> uris = events.stream()
                    .map(event -> "/events/" + event.getId())
                    .collect(Collectors.toList());
            try {
                ResponseEntity<List<ViewStats>> viewStats = getUrlViews(rangeStart, rangeEnd, uris, false);
                if ((viewStats.getStatusCode() == HttpStatus.OK) && (viewStats.getBody() != null)) {
                    Map<String, ViewStats> statsMap = viewStats.getBody().stream()
                            .collect(Collectors.toMap(ViewStats::getUri, Function.identity()));
                    events.forEach(event -> {
                        String key = "/events/" + event.getId();
                        if (statsMap.containsKey(key)) {
                            event.setViews(statsMap.get(key).getHits());
                        }
                    });
                }
            } catch (HttpClientErrorException e) {
                log.error("views not found : {} {} {}", rangeStart, rangeEnd, uris);
            }
        }
    }

    /**
     * Добавление данных о просмотрах в сокращенные DTO событий
     * @param events Сокращенные DTO событий
     * @param rangeStart Дата и время начала диапазона за который нужно выгрузить статистику
     * @param rangeEnd Дата и время конца диапазона за который нужно выгрузить статистику
     */
    public void setViewsForEventShortDtoList(
            @NotNull List<EventShortDto> events,
            @NotNull LocalDateTime rangeStart,
            @NotNull LocalDateTime rangeEnd) {
        events.forEach(event -> event.setViews(0L));
        if (events.size() > 0 && rangeStart != null && rangeEnd != null) {
            List<String> uris = events.stream()
                    .map(event -> "/events/" + event.getId())
                    .collect(Collectors.toList());
            try {
                ResponseEntity<List<ViewStats>> viewStats = getUrlViews(rangeStart, rangeEnd, uris, false);
                if ((viewStats.getStatusCode() == HttpStatus.OK) && (viewStats.getBody() != null)) {
                    Map<String, ViewStats> statsMap = viewStats.getBody().stream()
                            .collect(Collectors.toMap(ViewStats::getUri, Function.identity()));
                    events.forEach(event -> {
                        String key = "/events/" + event.getId();
                        if (statsMap.containsKey(key)) {
                            event.setViews(statsMap.get(key).getHits());
                        }
                    });
                }
            } catch (HttpClientErrorException e) {
                log.error("views not found : {} {} {}", rangeStart, rangeEnd, uris);
            }
        }
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
