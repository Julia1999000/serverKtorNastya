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


GET  http://127.0.0.1:8080/users/getInfo/{user_id}


PUT  http://127.0.0.1:8080/users/updateInfo/{user_id}
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "name": "", *
            "avatarId": "" *
        }


PUT  http://127.0.0.1:8080/users/updateSecretInfo/{user_id}
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "login": "", *
            "password": "" *
        }


DELETE  http://127.0.0.1:8080/users/del/{user_id}
    headers:
        Bearer-Authorization: "token"


GET  http://127.0.0.1:8080/users/getAll


DELETE  http://127.0.0.1:8080/users/unlogin/{user_id}
    headers:
        Bearer-Authorization: "token"


DELETE  http://127.0.0.1:8080/users/delAvatar/{user_id}
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


GET  http://127.0.0.1:8080/admins/getInfo/{admin_id}


PUT  http://127.0.0.1:8080/admins/updateInfo/{admin_id}
    headers:
        Bearer-Administration: "token"
    body (raw):
        {
            "name": "", *
            "avatarId": "" *
        }


PUT  http://127.0.0.1:8080/admins/updateSecretInfo/{admin_id}
    headers:
        Bearer-Administration: "token"
    body (raw):
        {
            "login": "", *
            "password": "" *
        }


DELETE  http://127.0.0.1:8080/admins/del/{admin_id}
    headers:
        Bearer-Administration: "token"


GET  http://127.0.0.1:8080/admins/getAll


DELETE  http://127.0.0.1:8080/admins/unlogin/{admin_id}
    headers:
        Bearer-Administration: "token"


DELETE  http://127.0.0.1:8080/admins/delAvatar/{admin_id}
    headers:
        Bearer-Administration: "token"

* */

/* COMS

POST  http://127.0.0.1:8080/coms/createByUser/{user_id}
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "description": "",
            "imageId": "" *
        }


GET  http://127.0.0.1:8080/coms/getAll/{user_id}
    headers:
        Bearer-Authorization: "token"

GET  http://127.0.0.1:8080/coms/getPublished/{user_id}


GET  http://127.0.0.1:8080/coms/getInfoById/{com_id}


PUT  http://127.0.0.1:8080/coms/publishByAdmin/{admin_id}/{com_id}
    headers:
        Bearer-Administration: "token"


PUT  http://127.0.0.1:8080/coms/checkByAdmin/{admin_id}/{com_id}
    headers:
        Bearer-Administration: "token"


DELETE  http://127.0.0.1:8080/coms/deleteByUser/{user_id}/{com_id}
    headers:
        Bearer-Authorization: "token"


DELETE  http://127.0.0.1:8080/coms/deleteByAdmin/{admin_id}/{com_id}
    headers:
        Bearer-Administration: "token"


GET  http://127.0.0.1:8080/coms/getAllPublished


GET  http://127.0.0.1:8080/coms/getAllCreated/{admin_id}
    headers:
        Bearer-Administration: "token"


* */

/* BOARDS

    //TODO

* */

/* FILE

POST  http://127.0.0.1:8080/file/images/uploadByUser/{user_id}
    headers:
        Bearer-Authorization: "token"
    body (form-data):
        file: FILE


POST  http://127.0.0.1:8080/file/images/uploadByAdmin/{admin_id}
    headers:
        Bearer-Administration: "token"
    body (form-data):
        file: FILE


GET  http://127.0.0.1:8080/file/images/download/{file_id}


GET  http://127.0.0.1:8080/file/images/open/{file_id}

* */