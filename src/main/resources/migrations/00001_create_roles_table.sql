--liquibase formatted sql

--changeset ghost:1

-- DROP TABLE public.roles;

CREATE TABLE public.roles (
   id int4 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
   role varchar(255) NULL,
   description varchar(255) NULL,
   CONSTRAINT roles_pkey PRIMARY KEY (id),
   CONSTRAINT roles_role_key UNIQUE (role)
);

--rollback DROP TABLE public.roles