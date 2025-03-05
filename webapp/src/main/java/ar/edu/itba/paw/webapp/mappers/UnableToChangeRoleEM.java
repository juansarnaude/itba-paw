package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.UnableToChangeRoleException;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Component
@Provider
public class UnableToChangeRoleEM implements ExceptionMapper<UnableToChangeRoleException> {
    @Override
    public Response toResponse(UnableToChangeRoleException e) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
    }
}
