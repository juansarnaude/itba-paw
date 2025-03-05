package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.ConflictException;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Singleton
@Component
@Provider
public class ConflictExceptionEM implements ExceptionMapper<ConflictException> {
    @
            Override
    public Response toResponse(ConflictException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ResponseMessage(exception.getMessage()))
                .build();
    }
}
