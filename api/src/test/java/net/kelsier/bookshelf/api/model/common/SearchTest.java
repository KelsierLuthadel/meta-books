package net.kelsier.bookshelf.api.model.common;

import io.dropwizard.jersey.validation.Validators;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.AuthorLookup;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.BookLookup;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.CommentLookup;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.DataLookup;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.LanguageLookup;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.PublisherLookup;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.RatingLookup;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.SeriesLookup;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.TagLookup;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class SearchTest {

    @Test
    void testValidSearch() {
        assertEquals(0, validate(
               new Search<>(new AuthorLookup("name", Operator.EQ, "test"),  getValidPagination()
               )).size(),"Search should be valid");

        assertEquals(0, validate(
                new Search<>(new BookLookup("title", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be valid");

        assertEquals(0, validate(
                new Search<>(new CommentLookup("text", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be valid");

        assertEquals(0, validate(
                new Search<>(new DataLookup("format", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be valid");

        assertEquals(0, validate(
                new Search<>(new LanguageLookup("lang_code", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be valid");

        assertEquals(0, validate(
                new Search<>(new PublisherLookup("name", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be valid");

        assertEquals(0, validate(
                new Search<>(new RatingLookup("rating", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be valid");

        assertEquals(0, validate(
                new Search<>(new SeriesLookup("name", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be valid");

        assertEquals(0, validate(
                new Search<>(new TagLookup("name", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be valid");
    }

    @Test
    void testInvalidSearchField() {
        assertEquals(1, validate(
                new Search<>(new AuthorLookup("unknown", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new BookLookup("unknown", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new CommentLookup("unknown", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new DataLookup("unknown", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new LanguageLookup("unknown", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new PublisherLookup("unknown", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new RatingLookup("unknown", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new SeriesLookup("unknown", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new TagLookup("unknown", Operator.EQ, "test"),  getValidPagination()
                )).size(),"Search should be invalid");
    }

    @Test
    void testInvalidSearchOperator() {
        assertEquals(1, validate(
                new Search<>(new AuthorLookup("name", Operator.GTE, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new CommentLookup("text", Operator.GTE, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new LanguageLookup("lang_code", Operator.GTE, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new PublisherLookup("name", Operator.GTE, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new RatingLookup("rating", Operator.LIKE, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new SeriesLookup("name", Operator.GT, "test"),  getValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(1, validate(
                new Search<>(new TagLookup("name", Operator.GT, "test"),  getValidPagination()
                )).size(),"Search should be invalid");
    }

    @Test
    void testInvalidPagination() {
        assertEquals(3, validate(
                new Search<>(new AuthorLookup("name", Operator.EQ, "test"),  getInValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(3, validate(
                new Search<>(new BookLookup("title", Operator.EQ, "test"),  getInValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(3, validate(
                new Search<>(new CommentLookup("text", Operator.EQ, "test"),  getInValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(3, validate(
                new Search<>(new DataLookup("format", Operator.EQ, "test"),  getInValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(3, validate(
                new Search<>(new LanguageLookup("lang_code", Operator.EQ, "test"),  getInValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(3, validate(
                new Search<>(new PublisherLookup("name", Operator.EQ, "test"),  getInValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(3, validate(
                new Search<>(new RatingLookup("rating", Operator.EQ, "test"),  getInValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(3, validate(
                new Search<>(new SeriesLookup("name", Operator.EQ, "test"),  getInValidPagination()
                )).size(),"Search should be invalid");

        assertEquals(3, validate(
                new Search<>(new TagLookup("name", Operator.EQ, "test"),  getInValidPagination()
                )).size(),"Search should be invalid");
    }


    Pagination getValidPagination() {
        return new Pagination(0, 10, new Sort("field", "asc"));
    }

    Pagination getInValidPagination() {
        return new Pagination(-1, 101, new Sort("field", "down"));
    }


    private Set<ConstraintViolation<Object>> validate(final Object object) {
        return Validators.newValidator().validate(object);
    }
}