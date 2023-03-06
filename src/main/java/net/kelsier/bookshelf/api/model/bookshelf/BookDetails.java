package net.kelsier.bookshelf.api.model.bookshelf;

import net.kelsier.bookshelf.framework.db.model.bookshelf.*;

import javax.validation.constraints.NotNull;

public class BookDetails {
    @NotNull
    private final Book book;
    private final Author author;
    private final Comment comment;
    private final BookData bookData;
    private final Language language;
    private final Publisher publisher;
    private final Rating rating;
    private final Series series;
    private final Tag tag;

    public BookDetails(final Book book,
                       final Author author,
                       final Comment comment,
                       final BookData bookData,
                       final Language language,
                       final Publisher publisher,
                       final Rating rating,
                       final Series series,
                       final Tag tag) {
        this.book = book;
        this.author = author;
        this.comment = comment;
        this.bookData = bookData;
        this.language = language;
        this.publisher = publisher;
        this.rating = rating;
        this.series = series;
        this.tag = tag;
    }

    public Book getBook() {
        return book;
    }

    public Author getAuthor() {
        return author;
    }

    public Comment getComment() {
        return comment;
    }

    public BookData getBookData() {
        return bookData;
    }

    public Language getLanguage() {
        return language;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public Rating getRating() {
        return rating;
    }

    public Series getSeries() {
        return series;
    }

    public Tag getTag() {
        return tag;
    }
}
