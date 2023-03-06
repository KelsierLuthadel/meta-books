--liquibase formatted sql

--changeset ghost:1

INSERT INTO public.users (
   username,
   password,
   roles,
   enabled
) VALUES (
 'admin',
 'quN2tqZgVe2tu3lPgwyIICpgKVCo5Bc+A7kPMqNFNQqNkGarL0ADT3UJfVqkrn6s',
 ARRAY [1, 2, 3, 4],
 true
)
