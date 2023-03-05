package net.kelsier.bookshelf.api.resource.bookshelf;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.model.bookshelf.SeriesLookup;
import net.kelsier.bookshelf.api.model.bookshelf.SeriesLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import net.kelsier.bookshelf.api.model.common.Pagination;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.api.model.common.Sort;
import net.kelsier.bookshelf.framework.db.dao.bookshelf.SeriesDAO;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Book;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class BooksSeriesTest {

    public static final String API = "/api/1/bookshelf/series";
    private Series series;
    private List<Series> seriesList;

    @Spy
    private SeriesDAO seriesDAO;

    @Mock
    static final Jdbi databaseConnection = mock(Jdbi.class);

    private static final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new BookSeries(databaseConnection))
            .build();

    @BeforeEach
    void setup() {
        series = new Series(1, "Book name", "Book name");

        seriesList = new ArrayList<>();

        seriesList.add(new Series(1, "Name 1", "1, Name"));
        seriesList.add(new Series(2, "Name 2", "2, Name"));
        seriesList.add(new Series(3, "Name 3", "3, Name"));

        seriesDAO = spy(SeriesDAO.class);

        when(databaseConnection.onDemand(SeriesDAO.class)).thenReturn(seriesDAO);

        when(seriesDAO.get(anyInt())).thenReturn(series);

        // limit, start, field, direction
        when(seriesDAO.find(anyInt(), anyInt(), anyString(), anyString())).thenReturn(seriesList);

        // value, field, operator, limit, start, field, direction
        when(seriesDAO.find(anyString(), anyString(), anyString(),
                anyInt(), anyInt(), anyString(), anyString())).thenReturn(seriesList);
    }

    @Test
    void testGetBook() {
        final Response post = resources.target(MessageFormat.format("{0}/1", API)).request().get();

        final Series response = post.readEntity(Series.class);
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        validateSeries(response, series);
    }

    private void validateSeries(final Series actual, final Series expected) {
        assertEquals(expected.getId(), actual.getId(), "id should match");
        assertEquals(expected.getSort(), actual.getSort(), "sort should match");
        assertEquals(expected.getName(), actual.getName(), "name should match");
    }

    @Test
    void testSearchBooks() {
        final Search<SeriesLookup> search = new Search<>(
                new SeriesLookup("name", Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        final List<Series> response = post.readEntity((new GenericType<>() {}));
        assertEquals(seriesList.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < seriesList.size(); i++) {
            validateSeries(response.get(i), seriesList.get(i));
        }

        verify(seriesDAO, times(1)).find("%value%", "name", "ILIKE",
                10, 0, "id", "asc");
    }

    @Test
    void testSearchBooksEquality() {
        final Search<SeriesLookup> search = new Search<>(
                new SeriesLookup("name", Operator.EQ, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        final List<Series> response = post.readEntity((new GenericType<>() {}));
        assertEquals(seriesList.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < seriesList.size(); i++) {
            validateSeries(response.get(i), seriesList.get(i));
        }

        verify(seriesDAO, times(1)).find("value", "name", "=",
                10, 0, "id", "asc");
    }

    @Test
    void testGetBooks() {
        final Search<SeriesLookup> search = new Search<>(
                null,
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        final List<Series> response = post.readEntity((new GenericType<>() {}));
        assertEquals(seriesList.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < seriesList.size(); i++) {
            validateSeries(response.get(i), seriesList.get(i));
        }

        verify(seriesDAO, times(1)).find(10, 0, "id", "asc");
    }

    @Test
    void testValidationForField() {
        final Search<SeriesLookup> search = new Search<>(
                new SeriesLookup("bad field", Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForOperator() {
        final Search<SeriesLookup> search = new Search<>(
                new SeriesLookup("name", null, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForPaginationStart() {
        final Search<SeriesLookup> search = new Search<>(
                new SeriesLookup("name", Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForPaginationLimit() {
        final Search<SeriesLookup> search = new Search<>(
                new SeriesLookup("name", Operator.LIKE, "value"),
                new Pagination(0, 101, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForPaginationSort() {
        final Search<SeriesLookup> search = new Search<>(
                new SeriesLookup("name", Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "random"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }


}