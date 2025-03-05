package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Component
@Provider
public class InvalidAccessToResourceEM implements ExceptionMapper<InvalidAccessToResourceException> {
    @Override
    public Response toResponse(InvalidAccessToResourceException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(new ResponseMessage("Unauthorized to do this action.")).build();
    }
}
