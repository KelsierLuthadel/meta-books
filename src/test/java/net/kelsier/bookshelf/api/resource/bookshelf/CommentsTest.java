package net.kelsier.bookshelf.api.resource.bookshelf;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.model.bookshelf.BookLookup;
import net.kelsier.bookshelf.api.model.bookshelf.CommentLookup;
import net.kelsier.bookshelf.api.model.bookshelf.SeriesLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import net.kelsier.bookshelf.api.model.common.Pagination;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.api.model.common.Sort;
import net.kelsier.bookshelf.framework.db.dao.bookshelf.CommentDAO;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Book;
import net.kelsier.bookshelf.framework.db.model.bookshelf.Comment;
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
class CommentsTest {

    public static final String API = "/api/1/bookshelf/comments";
    public static final String LOOKUP = "text";
    private Comment comment;
    private List<Comment> comments;

    @Spy
    private CommentDAO commentDAO;

    @Mock
    static final Jdbi databaseConnection = mock(Jdbi.class);

    private static final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new Comments(databaseConnection))
            .build();

    @BeforeEach
    void setup() {
        comment = new Comment(1, 1, "Book name");

        comments = new ArrayList<>();

        comments.add(new Comment(1, 1, "1, Name"));
        comments.add(new Comment(2, 1, "2, Name"));
        comments.add(new Comment(3, 3, "3, Name"));

        commentDAO = spy(CommentDAO.class);

        when(databaseConnection.onDemand(CommentDAO.class)).thenReturn(commentDAO);

        when(commentDAO.get(anyInt())).thenReturn(comment);

        // limit, start, field, direction
        when(commentDAO.find(anyInt(), anyInt(), anyString(), anyString())).thenReturn(comments);

        // value, field, operator, limit, start, field, direction
        when(commentDAO.find(anyString(), anyString(), anyString(),
                anyInt(), anyInt(), anyString(), anyString())).thenReturn(comments);
    }

    @Test
    void testGetComment() {
        final Response post = resources.target(MessageFormat.format("{0}/1", API)).request().get();

        final Comment response = post.readEntity(Comment.class);
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        validateComment(response, comment);
    }

    private void validateComment(final Comment actual, final Comment expected) {
        assertEquals(expected.getId(), actual.getId(), "id should match");
        assertEquals(expected.getBook(), actual.getBook(), "book should match");
        assertEquals(expected.getText(), actual.getText(), "text should match");
    }

    @Test
    void testSearchComments() {
        final Search<SeriesLookup> search = new Search<>(
                new SeriesLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        final List<Comment> response = post.readEntity((new GenericType<>() {}));
        assertEquals(comments.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < comments.size(); i++) {
            validateComment(response.get(i), comments.get(i));
        }

        verify(commentDAO, times(1)).find("%value%", LOOKUP, "ILIKE",
                10, 0, "id", "asc");
    }

    @Test
    void testGetComments() {
        final Search<CommentLookup> search = new Search<>(
                null,
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        final List<Comment> response = post.readEntity((new GenericType<>() {}));
        assertEquals(comments.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < comments.size(); i++) {
            validateComment(response.get(i), comments.get(i));
        }

        verify(commentDAO, times(1)).find(10, 0, "id", "asc");
    }

    @Test
    void testSearchEquality() {
        final Search<CommentLookup> search = new Search<>(
                new CommentLookup("text", Operator.EQ, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        final List<Comment> response = post.readEntity((new GenericType<>() {}));
        assertEquals(comments.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < comments.size(); i++) {
            validateComment(response.get(i), comments.get(i));
        }

        verify(commentDAO, times(1)).find("value", "text", "=",
                10, 0, "id", "asc");
    }

    @Test
    void testValidationForField() {
        final Search<CommentLookup> search = new Search<>(
                new CommentLookup("bad field", Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForOperator() {
        final Search<CommentLookup> search = new Search<>(
                new CommentLookup(LOOKUP, null, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForPaginationStart() {
        final Search<CommentLookup> search = new Search<>(
                new CommentLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForPaginationLimit() {
        final Search<CommentLookup> search = new Search<>(
                new CommentLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(0, 101, new Sort("id", "asc"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }

    @Test
    void testValidationForPaginationSort() {
        final Search<CommentLookup> search = new Search<>(
                new CommentLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "random"))
        );

        final Response post = resources.target(API).request().post(Entity.json(search));


        assertEquals(UNPROCESSABLE_ENTITY.getCode(),post.getStatus(),"Status should be 422 UNPROCESSABLE_ENTITY");
    }


}