package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Component
@Provider
public class UserNotLoggedEM implements ExceptionMapper<UserNotLoggedException> {
    @Override
    public Response toResponse(UserNotLoggedException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ResponseMessage("Unauthorized to do this action.")).build();
    }
}
