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

CREATE VIEW book_metadata AS select
    book.id, book.title,author.author_id, author.author,
    series.series,book.series_index,
    publisher.publisher,
    book.isbn,
    identifier.identifier_type, identifier.identifier_value,
    language.language ,
    data.format, data.size,
    book.has_cover, book.date_added, book.publication_date, book.last_modified, book.path,
    comments.comments,
    tag.tags
FROM books book

LEFT JOIN (
    SELECT ref.id AS id, authors.name  as author, authors.id as author_id
    FROM books ref
        INNER JOIN books_authors_link AS link ON link.book=ref.id
        INNER JOIN authors AS authors ON authors.id=link.author
    GROUP BY ref.id, authors.name, authors.id
) author USING(id)

LEFT JOIN (
    SELECT ref.id AS id, series.name as series
    FROM books ref
        INNER JOIN books_series_link AS link ON link.book=ref.id
        INNER JOIN series AS series ON series.id=link.series
    GROUP BY ref.id, series.name
) series USING(id)

LEFT JOIN (
    SELECT ref.id AS id, publishers.name as publisher
    FROM books ref
        INNER JOIN books_publishers_link AS link ON link.book=ref.id
        INNER JOIN publishers AS publishers ON publishers.id=link.publisher
    GROUP BY ref.id, publishers.name
) publisher USING(id)

LEFT JOIN (
    SELECT ref.id AS id, languages.lang_code as language
    FROM books ref
        INNER JOIN books_languages_link AS link ON link.book=ref.id
        INNER JOIN languages AS languages ON languages.id=link.lang_code
    GROUP BY ref.id, languages.lang_code
) language USING(id)

LEFT JOIN (
    SELECT ref.id AS id, data.format as format, data.uncompressed_size AS size
    FROM books ref
        INNER JOIN data AS data ON data.id=ref.id
) data USING(id)

LEFT JOIN (
    SELECT ref.id AS id, comments.text as comments
    FROM books ref
        INNER JOIN comments AS comments ON comments.book=ref.id
) comments USING(id)

LEFT JOIN (
    SELECT ref.id AS id, array_agg(tags.name) AS tags
    FROM books ref
        INNER JOIN books_tags_link AS link ON link.book=ref.id
        INNER JOIN tags AS tags ON tags.id=link.tag
    GROUP BY ref.id
) tag USING(id)

LEFT JOIN (
    SELECT ref.id AS id, array_agg(ident.type) AS identifier_type, array_agg(ident.val) as identifier_value
    FROM books ref
        JOIN identifiers AS ident on ident.book=ref.id
    GROUP BY ref.id
) identifier USING(id);

CREATE VIEW book_basic_metadata AS select
    book.id, book.title,author.author_id,author.author,
    series.series,book.series_index,
    publisher.publisher,
    book.isbn,
    language.language ,
    data.format, data.size,
    book.has_cover, book.publication_date, book.path,
    tag.tags
FROM books book

LEFT JOIN (
    SELECT ref.id AS id, authors.name  as author, authors.id as author_id
    FROM books ref
        INNER JOIN books_authors_link AS link ON link.book=ref.id
        INNER JOIN authors AS authors ON authors.id=link.author
    GROUP BY ref.id, authors.name, authors.id
) author USING(id)

LEFT JOIN (
    SELECT ref.id AS id, series.name as series
    FROM books ref
        INNER JOIN books_series_link AS link ON link.book=ref.id
        INNER JOIN series AS series ON series.id=link.series
    GROUP BY ref.id, series.name
) series USING(id)

LEFT JOIN (
    SELECT ref.id AS id, publishers.name as publisher
    FROM books ref
        INNER JOIN books_publishers_link AS link ON link.book=ref.id
        INNER JOIN publishers AS publishers ON publishers.id=link.publisher
    GROUP BY ref.id, publishers.name
) publisher USING(id)

LEFT JOIN (
    SELECT ref.id AS id, languages.lang_code as language
    FROM books ref
        INNER JOIN books_languages_link AS link ON link.book=ref.id
        INNER JOIN languages AS languages ON languages.id=link.lang_code
    GROUP BY ref.id, languages.lang_code
) language USING(id)

LEFT JOIN (
    SELECT ref.id AS id, data.format as format, data.uncompressed_size AS size
    FROM books ref
        INNER JOIN data AS data ON data.id=ref.id
) data USING(id)

LEFT JOIN (
    SELECT ref.id AS id, array_agg(tags.name) AS tags
    FROM books ref
        INNER JOIN books_tags_link AS link ON link.book=ref.id
        INNER JOIN tags AS tags ON tags.id=link.tag
    GROUP BY ref.id
) tag USING(id)



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