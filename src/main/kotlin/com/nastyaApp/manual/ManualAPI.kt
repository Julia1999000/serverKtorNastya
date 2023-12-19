package com.nastyaApp.manual

/* USERS

    POST  http://127.0.0.1:8080/users/registration
    body (raw):
        {
            "name": "",
            "login": "",
            "password": ""
        }


    POST  http://127.0.0.1:8080/users/login
    body (raw):
        {
            "login": "",
            "password": ""
        }


    GET  http://127.0.0.1:8080/users/getInfo/{id}


    PUT  http://127.0.0.1:8080/users/updateInfo/{id}
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "name": "", *
            "avatarId": "" *
        }


    PUT  http://127.0.0.1:8080/users/updateSecretInfo/{id}
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "login": "", *
            "password": "" *
        }


    DELETE  http://127.0.0.1:8080/users/del/{id}
    headers:
        Bearer-Authorization: "token"


    GET  http://127.0.0.1:8080/users/getAll


    DELETE  http://127.0.0.1:8080/users/unlogin/{id}
    headers:
        Bearer-Authorization: "token"


    DELETE  http://127.0.0.1:8080/users/delAvatar/{id}
    headers:
        Bearer-Authorization: "token"

*/

/* ADMINS

POST  http://127.0.0.1:8080/admins/registration
    body (raw):
        {
            "name": "",
            "login": "",
            "password": ""
        }


    POST  http://127.0.0.1:8080/admins/login
    body (raw):
        {
            "login": "",
            "password": ""
        }


    GET  http://127.0.0.1:8080/admins/getInfo/{id}


    PUT  http://127.0.0.1:8080/admins/updateInfo/{id}
    headers:
        Bearer-Administration: "token"
    body (raw):
        {
            "name": "", *
            "avatarId": "" *
        }


    PUT  http://127.0.0.1:8080/admins/updateSecretInfo/{id}
    headers:
        Bearer-Administration: "token"
    body (raw):
        {
            "login": "", *
            "password": "" *
        }


    DELETE  http://127.0.0.1:8080/admins/del/{id}
    headers:
        Bearer-Administration: "token"


    GET  http://127.0.0.1:8080/admins/getAll


    DELETE  http://127.0.0.1:8080/admins/unlogin/{id}
    headers:
        Bearer-Administration: "token"


    DELETE  http://127.0.0.1:8080/admins/delAvatar/{id}
    headers:
        Bearer-Administration: "token"

* */

/* COMS

    //TODO

* */

/* BOARDS

    //TODO

* */

/* FILE

    POST  http://127.0.0.1:8080/file/images/upload
    headers:
        Bearer-Authorization: "token"
    body (form-data):
        file: FILE


    GET  http://127.0.0.1:8080/file/images/download/{id}


    GET  http://127.0.0.1:8080/file/images/open/{id}

* */