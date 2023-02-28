package net.kelsier.bookshelf.framework.validator;

import io.dropwizard.jersey.validation.ConstraintMessage;
import io.dropwizard.jersey.validation.JerseyViolationException;
import org.glassfish.jersey.server.model.Invocable;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Set;

public class JerseyViolationMapper implements ExceptionMapper<JerseyViolationException> {
    @Override
    public Response toResponse(final JerseyViolationException exception) {
        final Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        final Invocable invocable = exception.getInvocable();
        final int status = ConstraintMessage.determineStatus(violations, invocable);
        return Response.status(status)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity(status >= 500 ? "Server error" : "Client error")
                .build();
    }
}


