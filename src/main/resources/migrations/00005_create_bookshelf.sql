--liquibase formatted sql

--changeset ghost:1

-- DROP TABLE public.authors;
-- DROP TABLE public.books;
-- DROP TABLE public.comments;
-- DROP TABLE public.custom_columns;
-- DROP TABLE public.identifiers;
-- DROP TABLE public.languages;
-- DROP TABLE public.publishers;
-- DROP TABLE public.ratings;
-- DROP TABLE public.series;
-- DROP TABLE public.tags;
-- DROP TABLE public.books_authors_link;
-- DROP TABLE public.books_languages_link;
-- DROP TABLE public.books_publishers_link;
-- DROP TABLE public.books_series_link;
-- DROP TABLE public.books_tags_link;

CREATE TABLE public.authors (
   id SERIAL PRIMARY KEY,
   name TEXT NOT NULL ,
   sort TEXT
);

CREATE TABLE public.books (
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL DEFAULT 'Unknown',
    sort TEXT ,
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    publication_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    series_index REAL NOT NULL DEFAULT 1.0,
    isbn TEXT DEFAULT '' ,
    path TEXT NOT NULL DEFAULT '',
    has_cover BOOL DEFAULT false,
    last_modified TIMESTAMP NOT NULL DEFAULT '2000-01-01 00:00:00+00:00'
);

CREATE TABLE public.comments (
    id SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    text TEXT NOT NULL ,
    UNIQUE(book)
);

CREATE TABLE public.data (
    id  SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    format TEXT NOT NULL ,
    uncompressed_size INTEGER NOT NULL,
    name TEXT NOT NULL,
    UNIQUE(book, format)
);

CREATE TABLE public.identifiers (
    id SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    type TEXT NOT NULL DEFAULT 'isbn' ,
    val  TEXT NOT NULL ,
    UNIQUE(book, type)
);

CREATE TABLE public.languages (
    id SERIAL PRIMARY KEY,
    lang_code TEXT NOT NULL ,
    UNIQUE(lang_code)
);

CREATE TABLE public.publishers (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL ,
    sort TEXT ,
    UNIQUE(name)
);

CREATE TABLE public.ratings (
    id SERIAL PRIMARY KEY,
    rating INTEGER CHECK(rating > -1 AND rating < 11),
    UNIQUE (rating)
);

CREATE TABLE public.series (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL ,
    sort TEXT ,
    UNIQUE (name)
);

CREATE TABLE public.tags (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL ,
    UNIQUE (name)
);

CREATE TABLE public.books_authors_link (
    id SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    author INTEGER NOT NULL,
    UNIQUE(book, author)
);

CREATE TABLE public.books_languages_link (
    id SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    lang_code INTEGER NOT NULL,
    UNIQUE(book, lang_code)
);

CREATE TABLE public.books_publishers_link (
    id SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    publisher INTEGER NOT NULL,
    UNIQUE(book)
);

CREATE TABLE public.books_ratings_link (
    id SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    rating INTEGER NOT NULL,
    UNIQUE(book, rating)
);

CREATE TABLE public.books_series_link (
    id SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    series INTEGER NOT NULL,
    UNIQUE(book)
);

CREATE TABLE public.books_tags_link (
    id SERIAL PRIMARY KEY,
    book INTEGER NOT NULL,
    tag INTEGER NOT NULL,
    UNIQUE(book, tag)
);

CREATE TABLE public.custom_columns (
    id SERIAL PRIMARY KEY,
    label TEXT NOT NULL,
    name TEXT NOT NULL,
    datatype TEXT NOT NULL,
    display  TEXT DEFAULT '{}' NOT NULL,
    is_multiple BOOL DEFAULT false NOT NULL,
    normalized BOOL NOT NULL,
    UNIQUE(label)
);

-- rollback DROP TABLE public.authors;
-- rollback DROP TABLE public.books;
-- rollback DROP TABLE public.comments;
-- rollback DROP TABLE public.custom_columns;
-- rollback DROP TABLE public.identifiers;
-- rollback DROP TABLE public.languages;
-- rollback DROP TABLE public.publishers;
-- rollback DROP TABLE public.ratings;
-- rollback DROP TABLE public.series;
-- rollback DROP TABLE public.tags;
-- rollback DROP TABLE public.books_authors_link;
-- rollback DROP TABLE public.books_languages_link;
-- rollback DROP TABLE public.books_publishers_link;
-- rollback DROP TABLE public.books_series_link;
-- rollback DROP TABLE public.books_tags_link;