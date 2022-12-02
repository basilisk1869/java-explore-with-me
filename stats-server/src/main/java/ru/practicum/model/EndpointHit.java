package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "endpoint_hits")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {

    /**
     * Идентификатор записи
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор сервиса для которого записывается информация
     */
    private String app;

    /**
     * посещенный путь
     */
    private String uri;

    /**
     * IP-адрес пользователя, осуществившего запрос
     */
    private String ip;

    /**
     * Дата и время, когда был совершен запрос к эндпоинту
     */
    @Column(name = "ts")
    private LocalDateTime timestamp;

}
