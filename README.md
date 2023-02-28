# MetaBooks


## Recreate database
drop database bookshelf
UPDATE databasechangelog SET MD5SUM = NULL;
create database bookshelf
\c bookshelf
GRANT ALL ON SCHEMA public TO bookshelf;

