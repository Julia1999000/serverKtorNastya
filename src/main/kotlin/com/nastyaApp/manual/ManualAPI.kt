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


    DELETE  http://127.0.0.1:8080/users/unlog/{id}
    headers:
        Bearer-Authorization: "token"


    DELETE  http://127.0.0.1:8080/users/delAvatar/{id}
    headers:
        Bearer-Authorization: "token"

*/

/* COMS

    //TODO

* */

/* BOARDS

    //TODO

* */

/* FILE

    POST  http://127.0.0.1:8080/file/images/upload
        body (form-data):
                file: FILE


    GET  http://127.0.0.1:8080/file/images/download/{id}


    GET  http://127.0.0.1:8080/file/images/open/{id}

* */