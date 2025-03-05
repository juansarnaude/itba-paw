package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.UserVerifiedException;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Singleton
@Component
@Provider
public class UserVerifiedEM implements ExceptionMapper<UserVerifiedException> {
    @Override
    public Response toResponse(UserVerifiedException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ResponseMessage(e.getMessage())).build();    }
}
