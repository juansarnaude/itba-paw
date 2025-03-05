package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.ResourceNotFoundException;
import ar.edu.itba.paw.models.Cast.Director;
import ar.edu.itba.paw.services.DirectorService;
import ar.edu.itba.paw.webapp.dto.out.DirectorDto;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("directors")
@Component
public class DirectorController {

    private final DirectorService directorService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GET
    @Produces(VndType.APPLICATION_DIRECTOR_LIST)
    public Response getDirectors(
            @QueryParam("search") final String search
    ) {
        try {
            if (search != null && !search.isEmpty()) {
                // Lógica para obtener directores por consulta de búsqueda
                List<Director> directors = directorService.getDirectorsForQuery(search, 5);
                List<DirectorDto> directorDtos = DirectorDto.fromDirectorList(directors, uriInfo);
                return Response.ok(new GenericEntity<List<DirectorDto>>(directorDtos) {}).build();
            }

            // Si no se proporcionan parámetros válidos
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("You must provide 'search' as query parameter.")
                    .build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the request.")
                    .build();
        }

    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_DIRECTOR)
    public Response getDirectorById(@PathParam("id") @NotNull final int directorId) {
        try {
            Director director = directorService.getDirectorById(directorId);
            return Response.ok(DirectorDto.fromDirector(director, uriInfo)).build();
        }catch (NoResultException e){
            throw new ResourceNotFoundException("Director not found");
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the request.")
                    .build();
        }

    }
}
