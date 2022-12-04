package ru.practicum.controller;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.service.StatsService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    @Autowired
    private final StatsService statsService;

    /**
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
     * @param endpointHitDto Название сервиса, uri и ip пользователя указаны в теле запроса.
     */
    @PostMapping(path = "/hit")
    void postEndpointHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("post hit : " + endpointHitDto.toString());
        statsService.postEndpointHit(endpointHitDto);
    }

    /**
     * Получение статистики по посещениям.
     * @param start Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris Список uri для которых нужно выгрузить статистику
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip)
     * @return Список записей оп посещениях.
     */
    @GetMapping(path = "/stats")
    List<ViewStats> getViewStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                 @RequestParam(required = false) List<String> uris,
                                 @RequestParam(required = false) Boolean unique) {
        log.info("get stats : " + start + " " + end + " " + uris + " " + unique);
        return statsService.getViewStats(start, end, uris, unique);
    }

}
