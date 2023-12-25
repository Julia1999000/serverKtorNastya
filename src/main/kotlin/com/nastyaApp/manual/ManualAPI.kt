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


GET  http://127.0.0.1:8080/coms/getAllByUser/{user_id}
    headers:
        Bearer-Authorization: "token"

GET  http://127.0.0.1:8080/coms/getPublishedByUser/{user_id}


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

POST  http://127.0.0.1:8080/boards/create/{user_id}
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "name": "",
            "status": ""
        }


DELETE  http://127.0.0.1:8080/boards/delete/{user_id}/{board_id}
    headers:
        Bearer-Authorization: "token"


PUT  http://127.0.0.1:8080/boards/update/{user_id}/{board_id}
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "name": "", *
            "status": "" *
        }


POST  http://127.0.0.1:8080/boards/addCom/{user_id}
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "comId": "",
            "boardId": ""
        }


DELETE  http://127.0.0.1:8080/boards/removeCom/{user_id}/{board_id}/{com_id}
    headers:
        Bearer-Authorization: "token"


GET  http://127.0.0.1:8080/boards/getAllComsByBoard/{board_id}


GET  http://127.0.0.1:8080/boards/getAllBoardsByUser/{user_id}
    headers:
        Bearer-Authorization: "token"

* */

/* LIKES

POST  http://127.0.0.1:8080/likes/set/{user_id}/{com_id}
    headers:
        Bearer-Authorization: "token"


DELETE  http://127.0.0.1:8080/likes/remove/{user_id}/{com_id}
    headers:
        Bearer-Authorization: "token"


GET  http://127.0.0.1:8080/likes/getAllByCom/{com_id}


* */

/* COMMENTS

POST  http://127.0.0.1:8080/comments/create/{user_id}
    headers:
        Bearer-Authorization: "token"
    body (raw):
        {
            "authorId": "",
            "comId": "",
            "text": ""
        }


DELETE  http://127.0.0.1:8080/comments/deleteByUser/{user_id}/{comment_id}
    headers:
        Bearer-Authorization: "token"


DELETE  http://127.0.0.1:8080/comments/deleteByAdmin/{admin_id}/{comment_id}
    headers:
        Bearer-Administration: "token"


GET  http://127.0.0.1:8080/comments/getAllByCom/{com_id}


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