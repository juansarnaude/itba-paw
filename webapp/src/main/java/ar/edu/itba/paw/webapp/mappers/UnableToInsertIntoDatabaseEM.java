package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
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
public class UnableToInsertIntoDatabaseEM implements ExceptionMapper<UnableToInsertIntoDatabase> {
    @Override
    public Response toResponse(UnableToInsertIntoDatabase exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ResponseMessage("Unable to insert into the database: " + exception.getMessage()))
                .build();
    }
}
