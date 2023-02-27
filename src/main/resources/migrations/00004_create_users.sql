--liquibase formatted sql

--changeset ghost:1

INSERT INTO public.users (
   username,
   password,
   roles,
   enabled
) VALUES (
 'admin',
 'admin',
 ARRAY [1, 2, 3, 4],
 true
)
