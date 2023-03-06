package net.kelsier.bookshelf.api.resource.bookshelf;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.DataLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import net.kelsier.bookshelf.api.model.common.Pagination;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.api.model.common.Sort;
import net.kelsier.bookshelf.api.db.dao.DataDAO;
import net.kelsier.bookshelf.api.db.model.BookData;
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
class DataTest {

    public static final String API = "/api/1/bookshelf/data";
    public static final String LOOKUP = "name";
    private BookData data;
    private List<BookData> dataList;

    @Spy
    private DataDAO dataDAO;

    @Mock
    static final Jdbi databaseConnection = mock(Jdbi.class);

    private static final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new Data(databaseConnection))
            .build();

    @BeforeEach
    void setup() {
        data = new BookData(1, 1, "format", 100, "name");

        dataList = new ArrayList<>();

        dataList.add(new BookData(1, 1, "format", 100, "name 1"));
        dataList.add(new BookData(2, 1, "format", 200, "name 2"));
        dataList.add(new BookData(3, 3, "format",150, "name 3"));

        dataDAO = spy(DataDAO.class);

        when(databaseConnection.onDemand(DataDAO.class)).thenReturn(dataDAO);

        when(dataDAO.get(anyInt())).thenReturn(data);

        // limit, start, field, direction
        when(dataDAO.find(anyInt(), anyInt(), anyString(), anyString())).thenReturn(dataList);

        when(dataDAO.find(anyInt(), anyString(),  anyString(), anyInt(), anyInt(), anyString(),  anyString())).thenReturn(dataList);

        // value, field, operator, limit, start, field, direction
        when(dataDAO.find(anyString(), anyString(), anyString(),
                anyInt(), anyInt(), anyString(), anyString())).thenReturn(dataList);
    }

    @Test
    void testGetBook() {
        final Response post = resources.target(MessageFormat.format("{0}/1", API)).request().get();

        final BookData  response = post.readEntity(BookData.class);
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        validateComment(response, data);
    }

    private void validateComment(final BookData actual, final BookData expected) {
        assertEquals(expected.getId(), actual.getId(), "id should match");
        assertEquals(expected.getBook(), actual.getBook(), "book should match");
        assertEquals(expected.getFormat(), actual.getFormat(), "format should match");
        assertEquals(expected.getUncompressedSize(), actual.getUncompressedSize(), "size should match");
        assertEquals(expected.getName(), actual.getName(), "name should match");
    }

    @Test
    void testSearch() {
        final Search<DataLookup> search = new Search<>(
                new DataLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<BookData> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(dataList.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < dataList.size(); i++) {
            validateComment(response.get(i), dataList.get(i));
        }

        verify(dataDAO, times(1)).find("%value%", LOOKUP, "ILIKE",
                10, 0, "id", "asc");
    }

    @Test
    void testSearchSize() {
        final Search<DataLookup> search = new Search<>(
                new DataLookup("uncompressed_size", Operator.EQ, "1024"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<BookData> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(dataList.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < dataList.size(); i++) {
            validateComment(response.get(i), dataList.get(i));
        }

        verify(dataDAO, times(1)).find(1024, "uncompressed_size", "=",
                10, 0, "id", "asc");
    }

    @Test
    void testGetBookData() {
        final Search<DataLookup> search = new Search<>(
                null,
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<BookData> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(dataList.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < dataList.size(); i++) {
            validateComment(response.get(i), dataList.get(i));
        }

        verify(dataDAO, times(1)).find(10, 0, "id", "asc");
    }

    @Test
    void testSearchEquality() {
        final Search<DataLookup> search = new Search<>(
                new DataLookup("format", Operator.EQ, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<BookData> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(dataList.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < dataList.size(); i++) {
            validateComment(response.get(i), dataList.get(i));
        }

        verify(dataDAO, times(1)).find("value", "format", "=",
                10, 0, "id", "asc");
    }

    @Test
    void testValidationForField() {
        final Search<DataLookup> search = new Search<>(
                new DataLookup("bad field", Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForOperator() {
        final Search<DataLookup> search = new Search<>(
                new DataLookup(LOOKUP, null, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationStart() {
        final Search<DataLookup> search = new Search<>(
                new DataLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationLimit() {
        final Search<DataLookup> search = new Search<>(
                new DataLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(0, 101, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationSort() {
        final Search<DataLookup> search = new Search<>(
                new DataLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "random"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }
}
