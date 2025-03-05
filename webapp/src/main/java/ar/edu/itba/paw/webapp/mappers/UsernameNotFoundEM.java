package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Component
@Provider
public class UsernameNotFoundEM implements ExceptionMapper<UsernameNotFoundException> {
    @Override
    public Response toResponse(UsernameNotFoundException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ResponseMessage("No users by that username.")).build();
    }
}
