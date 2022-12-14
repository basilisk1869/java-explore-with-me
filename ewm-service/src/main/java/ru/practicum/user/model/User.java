package ru.practicum.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Пользователь
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * Идентификатор
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя
     */
    private String name;

    /**
     * Электронная почта
     */
    @Column(unique = true)
    private String email;

    /**
     * Показывать рейтинг как инициатора событий и рейтинг своих событий
     */
    private Boolean showRating = false;

}
