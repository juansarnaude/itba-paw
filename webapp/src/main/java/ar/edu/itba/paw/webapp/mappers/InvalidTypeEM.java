package ar.edu.itba.paw.webapp.mappers;
import ar.edu.itba.paw.exceptions.InvalidTypeException;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Component
@Provider
public class InvalidTypeEM implements ExceptionMapper<InvalidTypeException> {
    @Override
    public Response toResponse(InvalidTypeException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}