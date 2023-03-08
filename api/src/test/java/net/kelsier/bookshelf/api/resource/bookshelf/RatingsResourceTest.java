package net.kelsier.bookshelf.api.resource.bookshelf;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.RatingLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import net.kelsier.bookshelf.api.model.common.Pagination;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.api.model.common.Sort;
import net.kelsier.bookshelf.api.db.dao.RatingDAO;
import net.kelsier.bookshelf.api.db.model.Rating;
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
class RatingsResourceTest {

    public static final String API = "/api/1/bookshelf/ratings";
    public static final String LOOKUP = "rating";
    private Rating rating;
    private List<Rating> ratings;

    @Spy
    private RatingDAO ratingDAO;

    @Mock
    static final Jdbi databaseConnection = mock(Jdbi.class);

    private static final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new RatingsResource(databaseConnection))
            .build();

    @BeforeEach
    void setup() {
        rating = new Rating(1, 1);

        ratings = new ArrayList<>();

        ratings.add(new Rating(1,  1));
        ratings.add(new Rating(2,  10));
        ratings.add(new Rating(3,  5));

        ratingDAO = spy(RatingDAO.class);

        when(databaseConnection.onDemand(RatingDAO.class)).thenReturn(ratingDAO);

        when(ratingDAO.get(anyInt())).thenReturn(rating);

        // limit, start, field, direction
        when(ratingDAO.find(anyInt(), anyInt(), anyString(), anyString())).thenReturn(ratings);

        // value, field, operator, limit, start, field, direction
        when(ratingDAO.find(anyDouble(), anyString(), anyString(),
                anyInt(), anyInt(), anyString(), anyString())).thenReturn(ratings);
    }

    @Test
    void testGet() {
        final Response post = resources.target(MessageFormat.format("{0}/1", API)).request().get();

        final Rating response = post.readEntity(Rating.class);
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        validateComment(response, rating);
    }

    private void validateComment(final Rating actual, final Rating expected) {
        assertEquals(expected.getId(), actual.getId(), "id should match");
        assertEquals(expected.getRating(), actual.getRating(), "rating should match");
    }

    @Test
    void testSearch() {
        final Search<RatingLookup> search = new Search<>(
                new RatingLookup(LOOKUP, Operator.EQ, "10"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Rating> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(ratings.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < ratings.size(); i++) {
            validateComment(response.get(i), ratings.get(i));
        }

        verify(ratingDAO, times(1)).find(10, LOOKUP, "=",
                10, 0, "id", "asc");
    }

    @Test
    void testGetAll() {
        final Search<RatingLookup> search = new Search<>(
                null,
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Rating> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(ratings.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < ratings.size(); i++) {
            validateComment(response.get(i), ratings.get(i));
        }

        verify(ratingDAO, times(1)).find(10, 0, "id", "asc");
    }

    @Test
    void testSearchEquality() {
        final Search<RatingLookup> search = new Search<>(
                new RatingLookup("rating", Operator.EQ, "10"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Rating> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(ratings.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < ratings.size(); i++) {
            validateComment(response.get(i), ratings.get(i));
        }

        verify(ratingDAO, times(1)).find(10, "rating", "=",
                10, 0, "id", "asc");
    }

    @Test
    void testValidationForField() {
        final Search<RatingLookup> search = new Search<>(
                new RatingLookup("bad field", Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForOperator() {
        final Search<RatingLookup> search = new Search<>(
                new RatingLookup(LOOKUP, null, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationStart() {
        final Search<RatingLookup> search = new Search<>(
                new RatingLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationLimit() {
        final Search<RatingLookup> search = new Search<>(
                new RatingLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(0, 101, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationSort() {
        final Search<RatingLookup> search = new Search<>(
                new RatingLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "random"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }
}
