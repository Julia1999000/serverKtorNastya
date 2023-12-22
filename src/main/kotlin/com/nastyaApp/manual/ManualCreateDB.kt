package com.nastyaApp.manual


/*

CREATE TABLE file_types(
id character varying(6) PRIMARY KEY NOT NULL
);


CREATE TABLE images(
id UUID PRIMARY KEY NOT NULL,
file bytea NOT NULL,
type_id character varying(6) REFERENCES file_types (id) ON DELETE CASCADE NOT NULL
);


INSERT INTO file_types
VALUES
('PNG'),
('JPG'),
('JPEG');


CREATE TABLE users(
id UUID PRIMARY KEY NOT NULL,
name text NOT NULL,
login text UNIQUE NOT NULL,
password text NOT NULL,
avatar_id UUID REFERENCES images (id) ON DELETE SET NULL
);


CREATE TABLE user_tokens(
token UUID PRIMARY KEY NOT NULL,
user_id UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL
);


CREATE TABLE admins(
id UUID PRIMARY KEY NOT NULL,
name text NOT NULL,
login text UNIQUE NOT NULL,
password text NOT NULL,
avatar_id UUID REFERENCES images (id) ON DELETE SET NULL
);


CREATE TABLE admin_tokens(
token UUID PRIMARY KEY NOT NULL,
admin_id UUID REFERENCES admins (id) ON DELETE CASCADE NOT NULL
);


CREATE TABLE com_statuses(
id character varying(10) PRIMARY KEY NOT NULL
);


INSERT INTO com_statuses
VALUES
('CREATED'),
('PUBLISHED'),
('CHECKABLE');


CREATE TABLE coms(
id UUID PRIMARY KEY NOT NULL,
description text NOT NULL,
image_id UUID REFERENCES images (id) ON DELETE SET NULL,
created_date timestamp NOT NULL,
author_id UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL,
status_id character varying(10) REFERENCES com_statuses (id) ON DELETE RESTRICT NOT NULL
);


CREATE TABLE comments(
id UUID PRIMARY KEY NOT NULL,
text text NOT NULL,
author_id UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL,
com_id UUID REFERENCES coms (id) ON DELETE CASCADE NOT NULL,
created_date timestamp NOT NULL
);


CREATE TABLE boards(
id UUID PRIMARY KEY NOT NULL,
name text NOT NULL,
owner_id UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL
);


CREATE TABLE likers_to_coms(
id UUID PRIMARY KEY NOT NULL,
com_id UUID REFERENCES coms (id) ON DELETE CASCADE NOT NULL,
user_id UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL
);


CREATE TABLE boards_to_coms(
id UUID PRIMARY KEY NOT NULL,
board_id UUID REFERENCES boards (id) ON DELETE CASCADE NOT NULL,
com_id UUID REFERENCES coms (id) ON DELETE CASCADE NOT NULL
);



CREATE OR REPLACE FUNCTION del_image_when_del_com()
RETURNS trigger
AS
$func$
BEGIN
DELETE FROM public.images WHERE id = OLD.image_id;
RETURN NULL;
END
$func$
LANGUAGE plpgsql;

CREATE TRIGGER com_image_delete
AFTER DELETE
ON public.coms
FOR EACH ROW
WHEN (OLD.image_id IS NOT NULL)
EXECUTE PROCEDURE del_image_when_del_com();


CREATE OR REPLACE FUNCTION del_image_when_del_user()
RETURNS trigger
AS
$func$
BEGIN
DELETE FROM public.images WHERE id = OLD.avatar_id;
RETURN NULL;
END
$func$
LANGUAGE plpgsql;

CREATE TRIGGER user_image_delete
AFTER DELETE
ON public.users
FOR EACH ROW
WHEN (OLD.avatar_id IS NOT NULL)
EXECUTE PROCEDURE del_image_when_del_user();


*/

