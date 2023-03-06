package net.kelsier.bookshelf.api.resource.bookshelf;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.LanguageLookup;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.TagLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import net.kelsier.bookshelf.api.model.common.Pagination;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.api.model.common.Sort;
import net.kelsier.bookshelf.framework.db.dao.bookshelf.TagDAO;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Tag;
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
class TagsTest {

    public static final String API = "/api/1/bookshelf/tags";
    public static final String LOOKUP = "name";
    private Tag tag;
    private List<Tag> tags;

    @Spy
    private TagDAO tagDAO;

    @Mock
    static final Jdbi databaseConnection = mock(Jdbi.class);

    private static final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new Tags(databaseConnection))
            .build();

    @BeforeEach
    void setup() {
        tag = new Tag(1, "code");

        tags = new ArrayList<>();

        tags.add(new Tag(1,  "code 1"));
        tags.add(new Tag(2,  "code 2"));
        tags.add(new Tag(3,  "code 3"));

        tagDAO = spy(TagDAO.class);

        when(databaseConnection.onDemand(TagDAO.class)).thenReturn(tagDAO);

        when(tagDAO.get(anyInt())).thenReturn(tag);

        // limit, start, field, direction
        when(tagDAO.find(anyInt(), anyInt(), anyString(), anyString())).thenReturn(tags);

        // value, field, operator, limit, start, field, direction
        when(tagDAO.find(anyString(), anyString(), anyString(),
                anyInt(), anyInt(), anyString(), anyString())).thenReturn(tags);
    }

    @Test
    void testGet() {
        final Response post = resources.target(MessageFormat.format("{0}/1", API)).request().get();

        final Tag response = post.readEntity(Tag.class);
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        validateComment(response, tag);
    }

    private void validateComment(final Tag actual, final Tag expected) {
        assertEquals(expected.getId(), actual.getId(), "id should match");
        assertEquals(expected.getName(), actual.getName(), "name should match");
    }

    @Test
    void testSearch() {
        final Search<LanguageLookup> search = new Search<>(
                new LanguageLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        final List<Tag> response = post.readEntity((new GenericType<>() {}));
        assertEquals(tags.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < tags.size(); i++) {
            validateComment(response.get(i), tags.get(i));
        }

        verify(tagDAO, times(1)).find("%value%", LOOKUP, "ILIKE",
                10, 0, "id", "asc");
    }

    @Test
    void testGetAll() {
        final Search<TagLookup> search = new Search<>(
                null,
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        final List<Tag> response = post.readEntity((new GenericType<>() {}));
        assertEquals(tags.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < tags.size(); i++) {
            validateComment(response.get(i), tags.get(i));
        }

        verify(tagDAO, times(1)).find(10, 0, "id", "asc");
    }

    @Test
    void testSearchEquality() {
        final Search<TagLookup> search = new Search<>(
                new TagLookup("name", Operator.EQ, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        final List<Tag> response = post.readEntity((new GenericType<>() {}));
        assertEquals(tags.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < tags.size(); i++) {
            validateComment(response.get(i), tags.get(i));
        }

        verify(tagDAO, times(1)).find("value", "name", "=",
                10, 0, "id", "asc");
    }

    @Test
    void testValidationForField() {
        final Search<TagLookup> search = new Search<>(
                new TagLookup("bad field", Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForOperator() {
        final Search<TagLookup> search = new Search<>(
                new TagLookup(LOOKUP, null, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForPaginationStart() {
        final Search<TagLookup> search = new Search<>(
                new TagLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForPaginationLimit() {
        final Search<TagLookup> search = new Search<>(
                new TagLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(0, 101, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForPaginationSort() {
        final Search<TagLookup> search = new Search<>(
                new TagLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "random"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }


}