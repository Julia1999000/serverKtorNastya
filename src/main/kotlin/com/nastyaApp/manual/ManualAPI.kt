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


PUT  http://127.0.0.1:8080/users/updateInfo
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "name": "", *
            "avatarId": "" *
        }


PUT  http://127.0.0.1:8080/users/updateSecretInfo
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "login": "", *
            "password": "" *
        }


DELETE  http://127.0.0.1:8080/users/delete
    headers:
        Bearer-Authorization: "token"


GET  http://127.0.0.1:8080/users/getAll


DELETE  http://127.0.0.1:8080/users/unlogin
    headers:
        Bearer-Authorization: "token"


DELETE  http://127.0.0.1:8080/users/delAvatar
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


PUT  http://127.0.0.1:8080/admins/updateInfo
    headers:
        Bearer-Administration: "token"
    body (raw):
        {
            "name": "", *
            "avatarId": "" *
        }


PUT  http://127.0.0.1:8080/admins/updateSecretInfo
    headers:
        Bearer-Administration: "token"
    body (raw):
        {
            "login": "", *
            "password": "" *
        }


DELETE  http://127.0.0.1:8080/admins/delete
    headers:
        Bearer-Administration: "token"


GET  http://127.0.0.1:8080/admins/getAll


DELETE  http://127.0.0.1:8080/admins/unlogin
    headers:
        Bearer-Administration: "token"


DELETE  http://127.0.0.1:8080/admins/delAvatar
    headers:
        Bearer-Administration: "token"

* */

/* COMS

POST  http://127.0.0.1:8080/coms/createByUser
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "description": "",
            "imageId": "" *
        }


GET  http://127.0.0.1:8080/coms/getAllByUser
    headers:
        Bearer-Authorization: "token"

GET  http://127.0.0.1:8080/coms/getPublishedByUser/{user_id}


GET  http://127.0.0.1:8080/coms/getInfoById/{com_id}


PUT  http://127.0.0.1:8080/coms/publishByAdmin/{com_id}
    headers:
        Bearer-Administration: "token"


PUT  http://127.0.0.1:8080/coms/checkByAdmin/{com_id}
    headers:
        Bearer-Administration: "token"


DELETE  http://127.0.0.1:8080/coms/deleteByUser/{com_id}
    headers:
        Bearer-Authorization: "token"


DELETE  http://127.0.0.1:8080/coms/deleteByAdmin/{com_id}
    headers:
        Bearer-Administration: "token"


GET  http://127.0.0.1:8080/coms/getAllPublished


GET  http://127.0.0.1:8080/coms/getAllCreatedByAdmin
    headers:
        Bearer-Administration: "token"

* */

/* BOARDS

POST  http://127.0.0.1:8080/boards/create
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "name": "",
            "status": ""
        }


DELETE  http://127.0.0.1:8080/boards/delete/{board_id}
    headers:
        Bearer-Authorization: "token"


PUT  http://127.0.0.1:8080/boards/update/{board_id}
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "name": "", *
            "status": "" *
        }


POST  http://127.0.0.1:8080/boards/addCom
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "comId": "",
            "boardId": ""
        }


DELETE  http://127.0.0.1:8080/boards/removeCom/{board_id}/{com_id}
    headers:
        Bearer-Authorization: "token"


GET  http://127.0.0.1:8080/boards/getAllComsByBoard/{board_id}


GET  http://127.0.0.1:8080/boards/getAllBoardsByUser
    headers:
        Bearer-Authorization: "token"

* */

/* LIKES

POST  http://127.0.0.1:8080/likes/set/{com_id}
    headers:
        Bearer-Authorization: "token"


DELETE  http://127.0.0.1:8080/likes/remove/{com_id}
    headers:
        Bearer-Authorization: "token"


GET  http://127.0.0.1:8080/likes/getAllByCom/{com_id}


* */

/* COMMENTS

POST  http://127.0.0.1:8080/comments/create
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "authorId": "",
            "comId": "",
            "text": ""
        }


DELETE  http://127.0.0.1:8080/comments/deleteByUser/{comment_id}
    headers:
        Bearer-Authorization: "token"


DELETE  http://127.0.0.1:8080/comments/deleteByAdmin/{comment_id}
    headers:
        Bearer-Administration: "token"


GET  http://127.0.0.1:8080/comments/getAllByCom/{com_id}


* */

/* FILE

POST  http://127.0.0.1:8080/file/images/uploadByUser
    headers:
        Bearer-Authorization: "token"
    body (form-data):
        file: FILE


POST  http://127.0.0.1:8080/file/images/uploadByAdmin
    headers:
        Bearer-Administration: "token"
    body (form-data):
        file: FILE


GET  http://127.0.0.1:8080/file/images/download/{file_id}


GET  http://127.0.0.1:8080/file/images/open/{file_id}

* */