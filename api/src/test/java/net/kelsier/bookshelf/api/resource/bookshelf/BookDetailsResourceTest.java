package net.kelsier.bookshelf.api.resource.bookshelf;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.db.dao.BookDetailsDAO;
import net.kelsier.bookshelf.api.db.model.Author;
import net.kelsier.bookshelf.api.db.model.BookDetails;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class BookDetailsResourceTest {

    public static final String API = "/api/1/bookshelf/1";
    private BookDetails details;

    @Spy
    private BookDetailsDAO bookDetailsDAO;

    @Mock
    static final Jdbi databaseConnection = mock(Jdbi.class);

    private static final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new BookDetailsResource(databaseConnection))
            .build();

    @BeforeEach
    void setup() {
        details = new BookDetails(1, "title", "author", "series",
                1, "publisher", "1234567890", "language",
                "format", 120, true, new Timestamp(0), new Timestamp(0), new Timestamp(0),
                "path", "comments");


        bookDetailsDAO = spy(BookDetailsDAO.class);

        when(databaseConnection.onDemand(BookDetailsDAO.class)).thenReturn(bookDetailsDAO);

        when(bookDetailsDAO.get(anyInt())).thenReturn(details);
    }

    @Test
    void getGetBook() {
        final Response post = resources.target(API).request().get();

        final Author response = post.readEntity(Author.class);
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        validateAuthor(details, response);
    }

    private void validateAuthor(final BookDetails expected, final Author response) {
        assertEquals(expected.getId(), response.getId(), "Author id should match");
    }




}