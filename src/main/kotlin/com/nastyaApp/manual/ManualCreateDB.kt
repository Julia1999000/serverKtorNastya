package com.nastyaApp.manual


/*

CREATE TABLE file_types(
id character varying(6) PRIMARY KEY NOT NULL
);


CREATE TABLE images(
id UUID PRIMARY KEY NOT NULL,
file bytea NOT NULL,
type_id character varying(6) REFERENCES file_types (id) ON DELETE CASCADE
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


CREATE TABLE tokens(
token UUID PRIMARY KEY NOT NULL,
user_id UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL
);


CREATE TABLE coms(
id UUID PRIMARY KEY NOT NULL,
description text NOT NULL,
image_id UUID REFERENCES images (id) ON DELETE SET NULL,
create_date date NOT NULL,
author_id UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL
);


CREATE TABLE comments(
id UUID PRIMARY KEY NOT NULL,
text text NOT NULL,
author_id UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL,
com_id UUID REFERENCES coms (id) ON DELETE CASCADE NOT NULL,
create_date date NOT NULL
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


*/

