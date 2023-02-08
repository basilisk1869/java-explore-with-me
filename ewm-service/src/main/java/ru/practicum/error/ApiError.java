package ru.practicum.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    /**
     * Список стектрейсов или описания ошибок
     */
    private List<String> errors;

    /**
     * Сообщение об ошибке
     */
    private String message;

    /**
     * Общее описание причины ошибки
     */
    private String reason;

    /**
     * Код статуса HTTP-ответа
     */
    private String status;

    /**
     * Дата и время когда произошла ошибка
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

}
