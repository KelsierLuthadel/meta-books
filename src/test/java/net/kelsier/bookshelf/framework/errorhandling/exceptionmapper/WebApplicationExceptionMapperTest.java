package net.kelsier.bookshelf.framework.errorhandling.exceptionmapper;

import net.kelsier.bookshelf.framework.error.exception.WebApplicationExceptionMapper;
import net.kelsier.bookshelf.framework.error.response.ResponseErrorFormat;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;


@SuppressWarnings("EmptyMethod")
public class WebApplicationExceptionMapperTest {
    @Test
    public void testToResponse() {
        final WebApplicationExceptionMapper mapper = new WebApplicationExceptionMapper();
        final WebApplicationException thrown = assertThrows(WebApplicationException.class, this::throwException);
        final Response response = mapper.toResponse(thrown);
        final ResponseErrorFormat error = (ResponseErrorFormat) response.getEntity();

        Assert.assertEquals(error.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        Assert.assertEquals("Something bad", error.getMessage());
        assertTrue(error.getErrors().isEmpty());
    }


    private void throwException() throws WebApplicationException {
        throw new WebApplicationException("Something bad");
    }
}