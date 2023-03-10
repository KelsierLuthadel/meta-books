package net.kelsier.bookshelf.api.resource.bookshelf;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.db.dao.BookDetailsDAO;
import net.kelsier.bookshelf.api.db.model.Author;
import net.kelsier.bookshelf.api.db.model.view.BookDetails;
import net.kelsier.bookshelf.api.db.model.view.Identifier;
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

    private static final String API = "/api/1/bookshelf/1";

    private static final int ID = 1;

    private static final String TITLE = "title";

    private static final String AUTHOR = "author";

    private static final String SERIES = "series";
    private static final int SERIES_INDEX = 3;

    public static final String PUBLISHER = "publisher";

    public static final String ISBN = "1234567890";

    public static final String[] IDENTIFIER_TYPES = new String [] {"provider-a", "provider-b", "provider-c"};

    public static final String[] IDENTIFIER_VALUES = {"book-reference-1", "book-reference-2", "book-reference-3"};

    public static final String LANGUAGE = "language";

    public static final String FORMAT = "format";

    public static final int SIZE = 120;

    public static final boolean HAS_COVER = true;

    public static final Timestamp DATE = new Timestamp(0);

    public static final String PATH = "path";

    public static final String COMMENTS = "comments";

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
        details = new BookDetails(ID, TITLE, AUTHOR, SERIES, SERIES_INDEX, PUBLISHER, ISBN,
            new Identifier(IDENTIFIER_TYPES, IDENTIFIER_VALUES), LANGUAGE, FORMAT, SIZE, HAS_COVER, DATE, DATE, DATE, PATH, COMMENTS);


        bookDetailsDAO = spy(BookDetailsDAO.class);

        when(databaseConnection.onDemand(BookDetailsDAO.class)).thenReturn(bookDetailsDAO);

        when(bookDetailsDAO.get(anyInt())).thenReturn(details);
    }

    @Test
    void testGetBook() {
        final Response post = resources.target(API).request().get();

        final Author response = post.readEntity(Author.class);
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        validateAuthor(details, response);
    }

    private void validateAuthor(final BookDetails expected, final Author response) {
        assertEquals(expected.getId(), response.getId(), "Author id should match");
    }




}