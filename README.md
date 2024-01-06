# Проект comFort
## Серверное приложение для работы с картинками. В нем можно регистрироваться, авторизовываться, публиковать картинки, писать комментарии, оценивать публикации, а также создавать собственные доски - подборки картинок на любую тематику.
## Наглядное представление структуры базы данных представлено ниже:
![структура бд](https://drive.google.com/file/d/15q3WVhdZNldmY_gNyaCIJmg2x9kaAU4L/view?usp=sharing)

## Используемые фреймворки
### `Ktor` для создания серверного приложения на языке Kotlin.
### `serialization-gson` для поддержки сериализации/десериализации данных в формате JSON с помощью библиотеки Gson.
### `Exposed` для работы с базами данных на языке Kotlin.
### `PostgreSQL` для работы с СУБД PostgreSQL.

## Клонирование репозитория
`git clone https://github.com/Julia1999000/serverKtorNastya.git`

## Запуск в IntelliJ IDEA Ultimate
### Необходимо завести перменные окружения: 
```
DB_URL
DB_USER
DB_PASSWORD
SERVER_HOST
SERVER_PORT
```

## Запуск приложения (docker)
`docker-compose up`

## Запуск машины minikube
`minikube start`

## Документация [API](https://test-my.postman.co/workspaces)

