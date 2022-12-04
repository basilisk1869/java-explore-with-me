package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitDto {

    /**
     * Идентификатор сервиса для которого записывается информация
     */
    private String app;

    /**
     * URI для которого был осуществлен запрос
     */
    private String uri;

    /**
     * IP-адрес пользователя, осуществившего запрос
     */
    private String ip;

    /**
     * Дата и время, когда был совершен запрос к эндпоинту
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
