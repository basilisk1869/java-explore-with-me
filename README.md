https://github.com/basilisk1869/java-explore-with-me/pull/1

# java-explore-with-me

## Идея

Свободное время — ценный ресурс. Ежедневно мы планируем, как его потратить — куда и с кем сходить.<br>
Сложнее всего в таком планировании поиск информации и переговоры. Какие намечаются мероприятия,<br>
свободны ли в этот момент друзья, как всех пригласить и где собраться.<br>
Поэтому нам нужно приложение-афиша, где можно предложить какое-либо событие от выставки до похода<br>
в кино и набрать компанию для участия в нём.<br>

## Исходный код

Исходный код проекта можно получить из GIT-репозитория:<br>
https://github.com/basilisk1869/java-explore-with-me

## Запуск приложения

Перейти в консоли в корневой каталог приложения.<br>
Выполнить сборку:<br>
`mvn clean package`<br>
Собрать docker-контейнеры:<br>
`docker-compose build`<br>
Запустить docker-контейнеры:<br>
`docker-compose up`<br>
Для остановки приложения нужно нажать сочетание клавиш Control + C<br>
и выполнить команду<br>
`docker-compose down`<br>

## Архитектура

Проект использует Spring Boot и реализует RestAPI для интеграции со сторонними проектами.
Приложение состоит из двух сервисов:<br>
ewm-service - основная функциональность приложения,<br>
stats-server - сбор статистики<br>
Каждый сервис взаимодействует со своей базой данных.<br>
![architecture.png](doc%2Farchitecture.png)

### ewm-service

Поднимается на сетевом порте 8080.<br>
Swagger: http://localhost:8080/swagger-ui/index.html <br>
Имеет следующую структуру БД:<br>
![ewm-schema.png](doc%2Fewm-schema.png)

### stats-server

Поднимается на сетевом порте 9090.<br>
Swagger: http://localhost:9090/swagger-ui/index.html <br>
Имеет следующую структуру БД:<br>
![stats-schema.png](doc%2Fstats-schema.png)
