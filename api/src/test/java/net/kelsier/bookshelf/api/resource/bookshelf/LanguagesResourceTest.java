package net.kelsier.bookshelf.api.resource.bookshelf;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import net.kelsier.bookshelf.api.model.bookshelf.lookup.LanguageLookup;
import net.kelsier.bookshelf.api.model.common.Operator;
import net.kelsier.bookshelf.api.model.common.Pagination;
import net.kelsier.bookshelf.api.model.common.Search;
import net.kelsier.bookshelf.api.model.common.Sort;
import net.kelsier.bookshelf.api.db.dao.LanguageDAO;
import net.kelsier.bookshelf.api.db.model.Language;
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
class LanguagesResourceTest {

    public static final String API = "/api/1/bookshelf/languages";
    public static final String LOOKUP = "lang_code";
    private Language language;
    private List<Language> languages;

    @Spy
    private LanguageDAO languageDAO;

    @Mock
    static final Jdbi databaseConnection = mock(Jdbi.class);

    private static final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new LanguagesResource(databaseConnection))
            .build();

    @BeforeEach
    void setup() {
        language = new Language(1, "code");

        languages = new ArrayList<>();

        languages.add(new Language(1,  "code 1"));
        languages.add(new Language(2,  "code 2"));
        languages.add(new Language(3,  "code 3"));

        languageDAO = spy(LanguageDAO.class);

        when(databaseConnection.onDemand(LanguageDAO.class)).thenReturn(languageDAO);

        when(languageDAO.get(anyInt())).thenReturn(language);

        // limit, start, field, direction
        when(languageDAO.find(anyInt(), anyInt(), anyString(), anyString())).thenReturn(languages);

        // value, field, operator, limit, start, field, direction
        when(languageDAO.find(anyString(), anyString(), anyString(),
                anyInt(), anyInt(), anyString(), anyString())).thenReturn(languages);
    }

    @Test
    void testGet() {
        final Response post = resources.target(MessageFormat.format("{0}/1", API)).request().get();

        final Language  response = post.readEntity(Language.class);
        assertEquals(Response.Status.OK.getStatusCode(),post.getStatus(),"Status should be 200 OK");

        validateComment(response, language);
    }

    private void validateComment(final Language actual, final Language expected) {
        assertEquals(expected.getId(), actual.getId(), "id should match");
        assertEquals(expected.getLanguageCode(), actual.getLanguageCode(), "language code should match");
    }

    @Test
    void testSearch() {
        final Search<LanguageLookup> search = new Search<>(
                new LanguageLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Language> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(languages.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < languages.size(); i++) {
            validateComment(response.get(i), languages.get(i));
        }

        verify(languageDAO, times(1)).find("%value%", LOOKUP, "ILIKE",
                10, 0, "id", "asc");
    }

    @Test
    void testGetAll() {
        final Search<LanguageLookup> search = new Search<>(
                null,
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Language> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(languages.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < languages.size(); i++) {
            validateComment(response.get(i), languages.get(i));
        }

        verify(languageDAO, times(1)).find(10, 0, "id", "asc");
    }

    @Test
    void testSearchEquality() {
        final Search<LanguageLookup> search = new Search<>(
                new LanguageLookup("lang_code", Operator.EQ, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        final List<Language> response;
        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(Response.Status.OK.getStatusCode(), post.getStatus(), "Status should be 200 OK");

            response = post.readEntity(new GenericType<>() {});
        }
        assertEquals(languages.size(), response.size(), "The correct number of results should be returned");

        for (int i = 0; i < languages.size(); i++) {
            validateComment(response.get(i), languages.get(i));
        }

        verify(languageDAO, times(1)).find("value", "lang_code", "=",
                10, 0, "id", "asc");
    }

    @Test
    void testValidationForField() {
        final Search<LanguageLookup> search = new Search<>(
                new LanguageLookup("bad field", Operator.LIKE, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForOperator() {
        final Search<LanguageLookup> search = new Search<>(
                new LanguageLookup(LOOKUP, null, "value"),
                new Pagination(0, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationStart() {
        final Search<LanguageLookup> search = new Search<>(
                new LanguageLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationLimit() {
        final Search<LanguageLookup> search = new Search<>(
                new LanguageLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(0, 101, new Sort("id", "asc"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }

    @Test
    void testValidationForPaginationSort() {
        final Search<LanguageLookup> search = new Search<>(
                new LanguageLookup(LOOKUP, Operator.LIKE, "value"),
                new Pagination(-1, 10, new Sort("id", "random"))
        );

        try (Response post = resources.target(API).request().post(Entity.json(search))) {
            assertEquals(UNPROCESSABLE_ENTITY.getCode(), post.getStatus(), "Status should be 422 UNPROCESSABLE_ENTITY");
        }
    }
}
