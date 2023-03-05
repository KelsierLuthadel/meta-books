package net.kelsier.bookshelf.api.resource.bookshelf;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.model.bookshelf.AuthorLookup;
import net.kelsier.bookshelf.api.model.bookshelf.SeriesLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import net.kelsier.bookshelf.api.model.common.Pagination;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.api.model.common.Sort;
import net.kelsier.bookshelf.framework.db.dao.bookshelf.AuthorDAO;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Author;
import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;

import net.kelsier.bookshelf.framework.db.model.bookshelf.Series;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class AuthorsTest {

    public static final String API = "/api/1/bookshelf/authors";
    private Author author;
    private List<Author> authors;

    @Spy
    private AuthorDAO authorDAOMock;

    @Mock
    static final Jdbi databaseConnection = mock(Jdbi.class);

    private static final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new Authors(databaseConnection))
            .build();

    @BeforeEach
    void setup() {
        author = new Author(1, "Author name", "Author name");
        authors = new ArrayList<>();

        authors.add(new Author(1, "Name 1", "1, Name"));
        authors.add(new Author(2, "Name 2", "2, Name"));
        authors.add(new Author(3, "Name 3", "3, Name"));
        authors.add(new Author(4, "Name 4", "4, Name"));
        authors.add(new Author(5, "Name 5", "5, Name"));
        authors.add(new Author(6, "Name 6", "6, Name"));
        authors.add(new Author(7, "Name 7", "7, Name"));
        authors.add(new Author(8, "Name 8", "8, Name"));

        authorDAOMock = spy(AuthorDAO.class);

        when(databaseConnection.onDemand(AuthorDAO.class)).thenReturn(authorDAOMock);

        when(authorDAOMock.get(anyInt())).thenReturn(author);

        // limit, start, field, direction
        when(authorDAOMock.get(anyInt(), anyInt(), anyString(), anyString())).thenReturn(authors);

        // value, field, operator, limit, start, field, direction
        when(authorDAOMock.find(anyString(), anyString(), anyString(),
                anyInt(), anyInt(), anyString(), anyString())).thenReturn(authors);
    }

    @Test
    void testGetAuthor() {
        final Response post = resources.target("/api/1/bookshelf/authors/1/").request().get();

        final Author response = post.readEntity(Author.class);
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        validateAuthor(author, response);
    }

    @Test
    void testSearchAuthors() {
        final Search<AuthorLookup> search = new Search<>(
                new AuthorLookup("name", Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        final List<Author> response = post.readEntity((new GenericType<>() {}));
        assertEquals(authors.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < authors.size(); i++) {
            validateAuthor(authors.get(i), response.get(i));
        }

        verify(authorDAOMock, times(1)).find("%value%", "name", "ILIKE",
                10, 0, "id", "asc");
    }

    @Test
    void testGetAuthors() {
        final Search<AuthorLookup> search = new Search<>(
                null,
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        final List<Author> response = post.readEntity((new GenericType<>() {}));
        assertEquals(authors.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < authors.size(); i++) {
            validateAuthor(authors.get(i), response.get(i));
        }

        verify(authorDAOMock, times(1)).get(10, 0, "id", "asc");
    }

    private void validateAuthor(final Author authors, final Author response) {
        assertEquals(authors.getId(), response.getId(), "Author id should match");
        assertEquals(authors.getName(), response.getName(), "Author name should match");
        assertEquals(authors.getSort(), response.getSort(), "Author sort should match");
    }

    @Test
    void testSearchEquality() {
        final Search<AuthorLookup> search = new Search<>(
                new AuthorLookup("name", Operator.EQ, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        final List<Author> response = post.readEntity((new GenericType<>() {}));
        assertEquals(authors.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < authors.size(); i++) {
            validateAuthor(response.get(i), authors.get(i));
        }

        verify(authorDAOMock, times(1)).find("value", "name", "=",
                10, 0, "id", "asc");
    }

    @Test
    void testValidationForField() {
        final Search<AuthorLookup> search = new Search<>(
                new AuthorLookup("bad field", Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForOperator() {
        final Search<AuthorLookup> search = new Search<>(
                new AuthorLookup("name", null, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForPaginationStart() {
        final Search<AuthorLookup> search = new Search<>(
                new AuthorLookup("name", Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForPaginationLimit() {
        final Search<AuthorLookup> search = new Search<>(
                new AuthorLookup("name", Operator.LIKE, "value"),
                new Pagination(0, 101, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForPaginationSort() {
        final Search<AuthorLookup> search = new Search<>(
                new AuthorLookup("name", Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "random"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }


}