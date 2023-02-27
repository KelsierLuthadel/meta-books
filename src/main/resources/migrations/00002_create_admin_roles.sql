--liquibase formatted sql

--changeset ghost:1

INSERT INTO public.roles (
  role,
  description
) VALUES (
 'admin:c',
 'Allow an administrator to create an entity'
),(
 'admin:r',
 'Allow an administrator to read an entity'
),(
 'admin:u',
 'Allow an administrator to update an entity'
),(
 'admin:d',
 'Allow an administrator to delete an entity'
),(
 'user:c',
 'Allow a user to create an entity'
),(
 'user:r',
 'Allow a user to read an entity'
),(
 'user:u',
 'Allow a user to update an entity'
),(
 'user:d',
 'Allow a user to delete an entity'
),(
   'guest:r',
   'Allow a guest to read an entity'
  )
