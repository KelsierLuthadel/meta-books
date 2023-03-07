package net.kelsier.bookshelf.framework.environment;

import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import org.junit.jupiter.api.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.mockito.Mockito.*;

class ResourceRegistrarTest {
    @Test
    void testRegisterObject() {
        final Environment environment = mock(Environment.class);
        final JerseyEnvironment jerseyEnv = mock(JerseyEnvironment.class);

        when(environment.jersey()).thenReturn(jerseyEnv);

        final ResourceRegistrar resourceRegistrar = new ResourceRegistrar(environment);
        final Object resource = new Object();

        resourceRegistrar.registerResource(resource);

        verify(jerseyEnv, times(1)).register(resource);
    }


    @SuppressWarnings("SameReturnValue")
    @Path("test_root")
    public static class TestResource {
        @GET
        @Path("test")
        @Produces(MediaType.TEXT_PLAIN)
        public String test() {
            return "TEST_TEST";
        }
    }
}
