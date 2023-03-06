package net.kelsier.bookshelf.framework.errorhandling;


import net.kelsier.bookshelf.framework.error.APIResponseError;
import net.kelsier.bookshelf.framework.error.exception.ResponseException;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


public class ResponseExceptionTest {
    @Test
    public void testException() {
        final ResponseException responseException = assertThrows(ResponseException.class,
            () -> throwException(null));

        assertEquals(responseException.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertEquals("Error", responseException.getMessage());
        assertEquals(2, (int) responseException.getErrorCode());
        assertEquals("help-url", responseException.getHelpUrl());
        Assert.assertNotNull(responseException.getErrors());
    }

    @Test
    public void testExceptionWithNoErrorCode() {
        final ResponseException responseException = assertThrows(ResponseException.class,
            () -> throwExceptionWithNoErrorCode(null));

        assertEquals(responseException.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertEquals("Error", responseException.getMessage());
        assertEquals("help-url", responseException.getHelpUrl());
        Assert.assertNotNull(responseException.getErrors());
    }

    @Test
    public void testExceptionWithNoErrorCodeAndResponseErrors() {
        final ResponseException responseException = assertThrows(ResponseException.class,
            () -> throwExceptionWithNoErrorCode(new ArrayList<>(Arrays.asList(
                new APIResponseError("First Error"),
                new APIResponseError("Second Error", "EOF missing"),
                new APIResponseError("Third Error", 2),
                new APIResponseError("Fourth Error", "EOF missing", 2)
            ))));

        assertEquals(responseException.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertEquals("Error", responseException.getMessage());
        assertEquals("help-url", responseException.getHelpUrl());
        Assert.assertNotNull(responseException.getErrors());

        checkResponseError(responseException);
    }

    private void throwException(final List<APIResponseError> errors) throws ResponseException {
        throw new ResponseException(
            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error", 2, "help-url", errors);
    }

    private void throwExceptionWithNoErrorCode(final List<APIResponseError> errors) throws ResponseException {
        throw new ResponseException(
            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error", "help-url", errors);
    }

    @Test
    public void testExceptionErrors() {
        final ResponseException responseException = assertThrows(ResponseException.class,
            () -> throwException(new ArrayList<>(Arrays.asList(
                new APIResponseError("First Error"),
                new APIResponseError("Second Error", "EOF missing"),
                new APIResponseError("Third Error", 2),
                new APIResponseError("Fourth Error", "EOF missing", 2)
            ))));

        checkResponseError(responseException);
    }

    private void checkResponseError(final ResponseException responseException) {
        Assert.assertNotNull(responseException.getErrors());
        assertEquals(4, responseException.getErrors().size());
        assertEquals("First Error", responseException.getErrors().get(0).getDetail());
        Assert.assertNull(responseException.getErrors().get(0).getCause());
        Assert.assertNull(responseException.getErrors().get(0).getErrorCode());

        assertEquals("Second Error", responseException.getErrors().get(1).getDetail());
        assertEquals("EOF missing", responseException.getErrors().get(1).getCause());
        Assert.assertNull(responseException.getErrors().get(1).getErrorCode());

        assertEquals("Third Error", responseException.getErrors().get(2).getDetail());
        Assert.assertNull(responseException.getErrors().get(2).getCause());
        assertEquals(2, (int) responseException.getErrors().get(2).getErrorCode());

        assertEquals("Fourth Error", responseException.getErrors().get(3).getDetail());
        assertEquals("EOF missing", responseException.getErrors().get(3).getCause());
        assertEquals(2, (int) responseException.getErrors().get(3).getErrorCode());
    }

    @Test
    public final void testDeepCopyOfResponseErrors() {
        final List<APIResponseError> errors = new ArrayList<>();
        errors.add(new APIResponseError("Hi"));
        final ResponseException exception = new ResponseException(1, null, null, errors);

        exception.getErrors().remove(0);
        assertEquals(1, exception.getErrors().size());
    }
}