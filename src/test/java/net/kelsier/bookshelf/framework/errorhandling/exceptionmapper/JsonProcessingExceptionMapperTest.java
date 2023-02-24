package net.kelsier.bookshelf.framework.errorhandling.exceptionmapper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import net.kelsier.bookshelf.framework.error.exception.JsonProcessingExceptionMapper;
import net.kelsier.bookshelf.framework.error.response.ResponseErrorFormat;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static org.eclipse.jetty.http.HttpStatus.Code.BAD_REQUEST;
import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;
import static org.junit.Assert.assertEquals;

public class JsonProcessingExceptionMapperTest {
    @Test
    public void testUnrecognizedPropertyException() throws Exception {
        try {
            new ObjectMapper().readValue("{\"unknown\":\"\"}", TestObject.class);
        } catch (final UnrecognizedPropertyException exception) {
            final ResponseErrorFormat responseErrorFormat = getResponseErrorFormat(exception);

            assertEquals(responseErrorFormat.getStatus(), UNPROCESSABLE_ENTITY.getCode());
            assertEquals("Unrecognized property 'unknown' at line 1, column 15", responseErrorFormat.getMessage());
        }
    }

    private ResponseErrorFormat getResponseErrorFormat(final JsonProcessingException exception) {
        final JsonProcessingExceptionMapper exceptionMapper = new JsonProcessingExceptionMapper();
        final Response response = exceptionMapper.toResponse(exception);
        return (ResponseErrorFormat) response.getEntity();
    }

    @Test
    public void testJsonParseException() throws Exception {
        try {
            new ObjectMapper().readValue("{\"test\": [", TestObject.class);
        } catch (final JsonParseException exception) {
            final ResponseErrorFormat responseErrorFormat = getResponseErrorFormat(exception);

            assertEquals(responseErrorFormat.getStatus(), BAD_REQUEST.getCode());
            assertEquals("Invalid JSON in request at line 1, column 11", responseErrorFormat.getMessage());
        }
    }

    @Test
    public void testException() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_IGNORED_PROPERTIES, true);

        try {
            mapper.readValue("{\"hello\":\"1\"}", TestObject.class);
        } catch (final JsonProcessingException exception) {
            final ResponseErrorFormat responseErrorFormat = getResponseErrorFormat(exception);

            assertEquals(responseErrorFormat.getStatus(), BAD_REQUEST.getCode());
            assertEquals("Bad Request at line 1, column 11", responseErrorFormat.getMessage());
        }
    }

    @Test
    public void testExceptionNoLocation() {
        final JsonParseException exception = Mockito.mock(JsonParseException.class);
        final ResponseErrorFormat responseErrorFormat = getResponseErrorFormat(exception);

        assertEquals(responseErrorFormat.getStatus(), BAD_REQUEST.getCode());
        assertEquals("Invalid JSON in request", responseErrorFormat.getMessage());
    }
}