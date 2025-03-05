package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.NoFileException;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Component
@Provider
public class NoFileEM implements ExceptionMapper<NoFileException> {
    @Override
    public Response toResponse(NoFileException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}