package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.UnableToBanUserException;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Component
@Provider
public class UnableToBanUserEM implements ExceptionMapper<UnableToBanUserException> {
    @Override
    public Response toResponse(UnableToBanUserException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(e.getMessage())
                .build();
    }
}
