package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Component
@Provider
public class AccessDeniedExceptionEM implements ExceptionMapper<AccessDeniedException> {
    @Override
    public Response toResponse(AccessDeniedException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(new ResponseMessage(e.getMessage())).build();
    }
}
