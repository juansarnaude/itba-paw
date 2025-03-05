package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.InvalidAuthenticationLevelRequiredToPerformActionException;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Component
@Provider
public class InvalidAuthenticationLevelRequiredToPerformActionEM implements ExceptionMapper<InvalidAuthenticationLevelRequiredToPerformActionException> {
    @Override
    public Response toResponse(InvalidAuthenticationLevelRequiredToPerformActionException e) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
    }
}
