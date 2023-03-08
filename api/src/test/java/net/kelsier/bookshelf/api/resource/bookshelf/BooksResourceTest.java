package net.kelsier.bookshelf.api.resource.bookshelf;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.BookLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import net.kelsier.bookshelf.api.model.common.Pagination;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.api.model.common.Sort;
import net.kelsier.bookshelf.api.db.dao.BookDAO;
import net.kelsier.bookshelf.api.db.model.Book;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(DropwizardExtensionsSupport.class)
class BooksResourceTest {

    public static final String API = "/api/1/bookshelf/books";
    private Book book;
    private List<Book> books;

    @Spy
    private BookDAO bookDAOMock;

    @Mock
    static final Jdbi databaseConnection = mock(Jdbi.class);

    private static final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new BooksResource(databaseConnection))
            .build();

    @BeforeEach
    void setup() {
        book = new Book(1, "Book name", "Book name",
                new Timestamp(0), new Timestamp(0), 1.0, "isbn", "/path", true, new Timestamp(0));
        books = new ArrayList<>();

        books.add(new Book(1, "Name 1", "1, Name",
                new Timestamp(0), new Timestamp(0), 1.0, "isbn", "/path", true,
                new Timestamp(0)));
        books.add(new Book(2, "Name 2", "2, Name",
                new Timestamp(0), new Timestamp(0), 1.0, "isbn", "/path", true,
                new Timestamp(0)));
        books.add(new Book(3, "Name 3", "3, Name",
                new Timestamp(0), new Timestamp(0), 1.0, "isbn", "/path", true,
                new Timestamp(0)));
        books.add(new Book(4, "Name 4", "4, Name",
                new Timestamp(0), new Timestamp(0), 1.0, "isbn", "/path", true,
                new Timestamp(0)));
        books.add(new Book(5, "Name 5", "5, Name",
                new Timestamp(0), new Timestamp(0), 1.0, "isbn", "/path", true,
                new Timestamp(0)));
        books.add(new Book(6, "Name 6", "6, Name",
                new Timestamp(0), new Timestamp(0), 1.0, "isbn", "/path", true,
                new Timestamp(0)));
        books.add(new Book(7, "Name 7", "7, Name",
                new Timestamp(0), new Timestamp(0), 1.0, "isbn", "/path", true,
                new Timestamp(0)));
        books.add(new Book(8, "Name 8", "8, Name",
                new Timestamp(0), new Timestamp(0), 1.0, "isbn", "/path", true,
                new Timestamp(0)));

        bookDAOMock = spy(BookDAO.class);

        when(databaseConnection.onDemand(BookDAO.class)).thenReturn(bookDAOMock);

        when(bookDAOMock.get(anyInt())).thenReturn(book);

        // limit, start, field, direction
        when(bookDAOMock.find(anyInt(), anyInt(), anyString(), anyString())).thenReturn(books);

        // value, field, operator, limit, start, field, direction
        when(bookDAOMock.find(anyString(), anyString(), anyString(),
                anyInt(), anyInt(), anyString(), anyString())).thenReturn(books);

        when(bookDAOMock.find(anyBoolean(), anyString(), anyString(),
                anyInt(), anyInt(), anyString(), anyString())).thenReturn(books);
    }

    @Test
    void testGetBook() {
        final Response post = resources.target(MessageFormat.format("{0}/1", API)).request().get();

        final Book response = post.readEntity(Book.class);
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        validateBook(response, book);
    }

    private void validateBook(final Book actual, final Book expected) {
        assertEquals(expected.getId(), actual.getId(), "Book id should match");
        assertEquals(expected.getTitle(), actual.getTitle(), "Book title should match");
        assertEquals(expected.getSort(), actual.getSort(), "Book sort should match");
        assertEquals(expected.getIsbn(), actual.getIsbn(), "Book isbn should match");
        assertEquals(expected.getPath(), actual.getPath(), "Book path should match");
        assertEquals(expected.getDateAdded(), actual.getDateAdded(), "Book date added should match");
        assertEquals(expected.getHasCover(), actual.getHasCover(), "Book cover should match");
        assertEquals(expected.getLastModified(), actual.getLastModified(), "Book last modified should match");
        assertEquals(expected.getSeriesIndex(), actual.getSeriesIndex(), "Book series index should match");
        assertEquals(expected.getPublicationDate(), actual.getPublicationDate(), "Book publication date should match");
    }

    @Test
    void testSearchBooks() {
        final Search<BookLookup> search = new Search<>(
                new BookLookup("title", Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Book> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");
            response = post.readEntity((new GenericType<>() {}));
        }

        assertEquals(books.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < books.size(); i++) {
            validateBook(response.get(i), books.get(i));
        }

        verify(bookDAOMock, times(1)).find("%value%", "title", "ILIKE",
                10, 0, "id", "asc");
    }

    @Test
    void testSearchBooksIsbn() {
        final Search<BookLookup> search = new Search<>(
                new BookLookup("isbn", Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Book> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity((new GenericType<>() {}));
        }
        assertEquals(books.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < books.size(); i++) {
            validateBook(response.get(i), books.get(i));
        }

        verify(bookDAOMock, times(1)).find("%value%", "isbn", "ILIKE",
                10, 0, "id", "asc");
    }

    @Test
    void testSearchBooksCover() {
        final Search<BookLookup> search = new Search<>(
                new BookLookup("has_cover", Operator.EQ, "true"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Book> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity((new GenericType<>() {}));
        }
        assertEquals(books.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < books.size(); i++) {
            validateBook(response.get(i), books.get(i));
        }

        verify(bookDAOMock, times(1)).find(true, "has_cover", "=",
                10, 0, "id", "asc");
    }

    @Test
    void testGetBooks() {
        final Search<BookLookup> search = new Search<>(
                null,
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Book> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity((new GenericType<>() {}));
        }
        assertEquals(books.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < books.size(); i++) {
            validateBook(response.get(i), books.get(i));
        }

        verify(bookDAOMock, times(1)).find(10, 0, "id", "asc");
    }

    @Test
    void testSearchEquality() {
        final Search<BookLookup> search = new Search<>(
                new BookLookup("title", Operator.EQ, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Book> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(books.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < books.size(); i++) {
            validateBook(response.get(i), books.get(i));
        }

        verify(bookDAOMock, times(1)).find("value", "title", "=",
                10, 0, "id", "asc");
    }

    @Test
    void testValidationForField() {
        final Search<BookLookup> search = new Search<>(
                new BookLookup("bad field", Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForOperator() {
        final Search<BookLookup> search = new Search<>(
                new BookLookup("name", null, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationStart() {
        final Search<BookLookup> search = new Search<>(
                new BookLookup("name", Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationLimit() {
        final Search<BookLookup> search = new Search<>(
                new BookLookup("name", Operator.LIKE, "value"),
                new Pagination(0, 101, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationSort() {
        final Search<BookLookup> search = new Search<>(
                new BookLookup("name", Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "random"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }
}
