package net.kelsier.bookshelf.api.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/1/books")
@Produces({"application/json", "application/xml"})

public class Bookshelf {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bookshelf.class);

    public Bookshelf() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get Books",
        tags = {"Bookshelf"},
        description = "Get a list of books",
        responses = {
            @ApiResponse(responseCode = "200")
        })

    public Response books()  {
        return Response.ok().build();
    }

}
