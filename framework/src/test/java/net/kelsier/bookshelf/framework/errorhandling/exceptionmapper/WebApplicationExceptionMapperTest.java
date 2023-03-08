package net.kelsier.bookshelf.framework.errorhandling.exceptionmapper;

import net.kelsier.bookshelf.framework.error.exception.WebApplicationExceptionMapper;
import net.kelsier.bookshelf.framework.error.response.ResponseErrorFormat;
import org.junit.jupiter.api.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

class WebApplicationExceptionMapperTest {
    @Test
    void testToResponse() {
        final WebApplicationExceptionMapper mapper = new WebApplicationExceptionMapper();
        final WebApplicationException thrown = assertThrows(WebApplicationException.class, this::throwException);
        try(final Response response = mapper.toResponse(thrown)) {
            final ResponseErrorFormat error = (ResponseErrorFormat) response.getEntity();

            assertEquals(error.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            assertEquals("Something bad", error.getMessage());
            assertTrue(error.getErrors().isEmpty());
        }
    }


    private void throwException() throws WebApplicationException {
        throw new WebApplicationException("Something bad");
    }
}