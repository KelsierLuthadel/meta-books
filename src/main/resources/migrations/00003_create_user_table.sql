--liquibase formatted sql

--changeset ghost:1

-- DROP TABLE public.users;

CREATE TABLE public.users (
   id int4 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
   username varchar(255) NOT NULL,
   firstname varchar(255) NULL,
   lastname varchar(255) NULL,
   email varchar(255) NULL,
   enabled BOOLEAN NOT NULL,
   password varchar(255) NOT NULL,
   roles int4[] NOT NULL,
   CONSTRAINT users_pkey PRIMARY KEY (id),
   CONSTRAINT users_user_key UNIQUE (username)
);

--rollback DROP TABLE public.users