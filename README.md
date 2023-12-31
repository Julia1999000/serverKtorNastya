<div id="header" align="center">
  <img src="https://github.com/Julia1999000/serverKtorNastya/assets/99553591/d410d41e-f0f4-4d97-9000-6e4f16810a50" width="500">
</div>


# Проект comFort
## Серверное приложение для работы с картинками. В нем можно регистрироваться, авторизовываться, публиковать картинки, писать комментарии, оценивать публикации, а также создавать собственные доски - подборки картинок на любую тематику.
## Наглядное представление структуры базы данных представлено ниже:
![структура бд](https://github.com/Julia1999000/serverKtorNastya/assets/99553591/8d1a1a68-59d6-4687-bf26-23420d1b8f87)

## Используемые фреймворки

`Ktor` для создания серверного приложения на языке Kotlin.

`serialization-gson` для поддержки сериализации/десериализации данных в формате JSON с помощью библиотеки Gson.

`Exposed` для работы с базами данных на языке Kotlin.

`PostgreSQL` для работы с СУБД PostgreSQL.

## Клонирование репозитория
```
git clone https://github.com/Julia1999000/serverKtorNastya.git
```

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
```
docker-compose up
```

## Запуск машины minikube
```
minikube start
```

## Запуск сервиса minikube
```
minikube service gradle-app-service
```

## Документация [API](https://test-my.postman.co/workspaces)

## Подгрузить образ
```
docker pull nastyaakaply/comfort
```
