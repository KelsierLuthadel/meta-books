package net.kelsier.bookshelf.api.resource.bookshelf;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.PublisherLookup;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.LanguageLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import net.kelsier.bookshelf.api.model.common.Pagination;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.api.model.common.Sort;
import net.kelsier.bookshelf.api.db.dao.metadata.PublisherDAO;
import net.kelsier.bookshelf.api.db.model.metadata.Publisher;
import net.kelsier.bookshelf.api.resource.bookshelf.metadata.PublishersResource;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(DropwizardExtensionsSupport.class)
class PublisherResourceTest {

    public static final String API = "/api/1/bookshelf/publishers";
    public static final String LOOKUP = "name";
    private Publisher publisher;
    private List<Publisher> publishers;

    @Spy
    private PublisherDAO publisherDAO;

    @Mock
    static final Jdbi databaseConnection = mock(Jdbi.class);

    private static final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new PublishersResource(databaseConnection))
            .build();

    @BeforeEach
    void setup() {
        publisher = new Publisher(1, "code", "code");

        publishers = new ArrayList<>();

        publishers.add(new Publisher(1,  "code 1", "1, code"));
        publishers.add(new Publisher(2,  "code 2", "2, code"));
        publishers.add(new Publisher(3,  "code 3", "3, code"));

        publisherDAO = spy(PublisherDAO.class);

        when(databaseConnection.onDemand(PublisherDAO.class)).thenReturn(publisherDAO);

        when(publisherDAO.get(anyInt())).thenReturn(publisher);

        // limit, start, field, direction
        when(publisherDAO.find(anyInt(), anyInt(), anyString(), anyString())).thenReturn(publishers);

        // value, field, operator, limit, start, field, direction
        when(publisherDAO.find(anyString(), anyString(), anyString(),
                anyInt(), anyInt(), anyString(), anyString())).thenReturn(publishers);
    }

    @Test
    void testGet() {
        final Response post = resources.target(MessageFormat.format("{0}/1", API)).request().get();

        final Publisher  response = post.readEntity(Publisher.class);
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        validateComment(response, publisher);
    }

    private void validateComment(final Publisher actual, final Publisher expected) {
        assertEquals(expected.getId(), actual.getId(), "id should match");
        assertEquals(expected.getName(), actual.getName(), "name should match");
        assertEquals(expected.getSort(), actual.getSort(), "sort should match");
    }

    @Test
    void testSearch() {
        final Search<LanguageLookup> search = new Search<>(
                new LanguageLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Publisher> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(publishers.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < publishers.size(); i++) {
            validateComment(response.get(i), publishers.get(i));
        }

        verify(publisherDAO, times(1)).find("%value%", LOOKUP, "ILIKE",
                10, 0, "id", "asc");
    }

    @Test
    void testGetAll() {
        final Search<PublisherLookup> search = new Search<>(
                null,
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Publisher> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(publishers.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < publishers.size(); i++) {
            validateComment(response.get(i), publishers.get(i));
        }

        verify(publisherDAO, times(1)).find(10, 0, "id", "asc");
    }

    @Test
    void testSearchEquality() {
        final Search<PublisherLookup> search = new Search<>(
                new PublisherLookup("name", Operator.EQ, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Publisher> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(publishers.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < publishers.size(); i++) {
            validateComment(response.get(i), publishers.get(i));
        }

        verify(publisherDAO, times(1)).find("value", "name", "=",
                10, 0, "id", "asc");
    }

    @Test
    void testValidationForField() {
        final Search<PublisherLookup> search = new Search<>(
                new PublisherLookup("bad field", Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForOperator() {
        final Search<PublisherLookup> search = new Search<>(
                new PublisherLookup(LOOKUP, null, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationStart() {
        final Search<PublisherLookup> search = new Search<>(
                new PublisherLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationLimit() {
        final Search<PublisherLookup> search = new Search<>(
                new PublisherLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(0, 101, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationSort() {
        final Search<PublisherLookup> search = new Search<>(
                new PublisherLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "random"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }
}
