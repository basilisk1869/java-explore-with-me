package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStats {

    /**
     * Идентификатор сервиса для которого записывается информация
     */
    private String app;

    /**
     * URI для которого был осуществлен запрос
     */
    private String uri;

    /**
     * Количество просмотров
     */
    private Long hits;

}
