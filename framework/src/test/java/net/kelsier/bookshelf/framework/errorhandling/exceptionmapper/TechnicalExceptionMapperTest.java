package net.kelsier.bookshelf.framework.errorhandling.exceptionmapper;

import net.kelsier.bookshelf.framework.error.exception.TechnicalExceptionMapper;
import net.kelsier.bookshelf.framework.error.response.ResponseErrorFormat;
import org.junit.jupiter.api.Test;
import org.pac4j.core.exception.TechnicalException;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

class TechnicalExceptionMapperTest {
    @Test
    void testToResponse() {
        final TechnicalExceptionMapper mapper = new TechnicalExceptionMapper();
        final TechnicalException thrown = assertThrows(TechnicalException.class, this::throwException);
        try(final Response response = mapper.toResponse(thrown)) {
            final ResponseErrorFormat error = (ResponseErrorFormat) response.getEntity();

            assertEquals(error.getStatus(), Response.Status.UNAUTHORIZED.getStatusCode());
            assertEquals("Something bad", error.getMessage());
            assertTrue(error.getErrors().isEmpty());
        }
    }

    private void throwException() throws TechnicalException {
        throw new TechnicalException("Something bad");
    }
}