package net.kelsier.bookshelf.api.model.bookshelf;

import net.kelsier.bookshelf.api.db.model.Author;
import net.kelsier.bookshelf.api.db.model.Book;
import net.kelsier.bookshelf.api.db.model.BookData;
import net.kelsier.bookshelf.api.db.model.Comment;
import net.kelsier.bookshelf.api.db.model.Language;
import net.kelsier.bookshelf.api.db.model.Publisher;
import net.kelsier.bookshelf.api.db.model.Rating;
import net.kelsier.bookshelf.api.db.model.Series;
import net.kelsier.bookshelf.api.db.model.Tag;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class BookDetails {
    @NotNull
    private final @Valid Book book;
    private final @Valid Author author;
    private final @Valid Comment comment;
    private final @Valid BookData bookData;
    private final @Valid Language language;
    private final @Valid Publisher publisher;
    private final @Valid Rating rating;
    private final @Valid Series series;
    private final @Valid Tag tag;

    public BookDetails(@Valid final Book book,
                       @Valid final Author author,
                       @Valid final Comment comment,
                       @Valid final BookData bookData,
                       @Valid final Language language,
                       @Valid final Publisher publisher,
                       @Valid final Rating rating,
                       @Valid final Series series,
                       @Valid final Tag tag) {
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
