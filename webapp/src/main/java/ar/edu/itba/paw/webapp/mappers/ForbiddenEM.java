package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Component
@Provider
public class ForbiddenEM implements ExceptionMapper<ForbiddenException> {
    @Override
    public Response toResponse(ForbiddenException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(new ResponseMessage(e.getMessage())).build();
    }
}
