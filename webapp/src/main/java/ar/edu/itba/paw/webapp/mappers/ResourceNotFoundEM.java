package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.ResourceNotFoundException;
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
public class ResourceNotFoundEM implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ResponseMessage(e.getMessage())).build();
    }
}