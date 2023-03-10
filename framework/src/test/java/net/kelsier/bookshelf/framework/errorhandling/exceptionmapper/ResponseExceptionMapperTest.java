package net.kelsier.bookshelf.framework.errorhandling.exceptionmapper;

import net.kelsier.bookshelf.framework.error.APIResponseError;
import net.kelsier.bookshelf.framework.error.exception.ResponseErrorExceptionMapper;
import net.kelsier.bookshelf.framework.error.exception.ResponseException;
import net.kelsier.bookshelf.framework.error.response.ResponseErrorFormat;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResponseExceptionMapperTest {
    @Test
    void testToResponse() {
        final ResponseErrorExceptionMapper mapper = new ResponseErrorExceptionMapper();
        final ResponseException thrown = assertThrows(ResponseException.class, this::throwException);

        try(final Response response = mapper.toResponse(thrown)) {
            final ResponseErrorFormat error = (ResponseErrorFormat) response.getEntity();

            assertEquals(error.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            assertEquals("Error", error.getMessage());
            assertEquals(1, (int) error.getErrorCode());
            assertEquals(3, error.getErrors().size());
            assertEquals(2, (int) error.getErrors().get(2).getErrorCode());
            assertEquals("help-url", error.getHelpUrl());
            final String reference = error.getReference();
            assertEquals(36, reference.length());
        }
    }

    private void throwException() throws ResponseException {
        final List<APIResponseError> errors = new ArrayList<>(Arrays.asList(
            new APIResponseError("First Error"),
            new APIResponseError("Second Error", "EOF missing"),
            new APIResponseError("Third Error", "EOF missing", 2)
        ));

        throw new ResponseException(
            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error", 1, "help-url", errors);
    }
}