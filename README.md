# diplomaProject - это дипломный проект Spring API для управления файлами
Этот проект представляет собой приложение на Spring, которое реализует API для аутентификации и управления файлами.
## Особенности
- Аутентификация пользователя с использованием хэша логина и пароля
- Загрузка, скачивание, удаление и обновление файлов
- Получение списка всех файлов на сервере
- Использование JSON и multipart/form-data для запросов и ответов
- Требование токена аутентификации для доступа к большинству конечных точек

## Требования

- Java 11 или выше
- Maven
- PostgreSQL

## Установка

1. Клонируйте репозиторий приложения на свой компьютер.
2. Откройте файл `application.properties` и измените настройки подключения к базе данных в соответствии с вашей конфигурацией.
3. Запустите команду `mvn clean install` для сборки приложения.
4. Запустите команду `java -jar target/myapp.jar` для запуска приложения.

## Использование

Откройте веб-браузер и перейдите по адресу `http://localhost:8085`. Вы увидите страницу входа в приложение. Введите свои учетные данные и нажмите кнопку "Войти".

На главной странице вы увидите список своих файлов. Вы можете загрузить новый файл, нажав на кнопку "Загрузить", скачать файл, нажав на ссылку "Скачать", удалить файл, нажав на ссылку "Удалить", или переименовать файл, нажав на ссылку "Переименовать".

## Конфигурация

Вы можете изменить настройки приложения, отредактировав файл `application.properties`. Например, вы можете изменить порт, на котором запускается приложение, изменив значение параметра `server.port`.

## Лицензия

Приложение распространяется под лицензией MIT.