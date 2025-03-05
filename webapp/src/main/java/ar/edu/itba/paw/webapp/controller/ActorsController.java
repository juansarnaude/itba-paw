package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.services.ActorService;
import ar.edu.itba.paw.webapp.dto.out.ActorDto;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.NoSuchElementException;

@Path("actors")
@Component
public class ActorsController {

    private final ActorService actorService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public ActorsController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GET
    @Produces(VndType.APPLICATION_ACTOR_LIST)
    public Response getActors(
            @QueryParam("mediaId") final Integer mediaId,
            @QueryParam("search") final String search
    ) {
        try {
            if (mediaId != null) {
                // Buscar actores por mediaId
                final List<ActorDto> actorDtoList = ActorDto.fromActorList(actorService.getAllActorsForMedia(mediaId), uriInfo);
                return Response.ok(new GenericEntity<List<ActorDto>>(actorDtoList) {}).build();
            } else if (search != null && !search.isEmpty()) {
                // Buscar actores por consulta de texto
                List<Actor> actorList = actorService.getActorsForQuery(search);
                List<ActorDto> actorDtoList = ActorDto.fromActorList(actorList, uriInfo);
                return Response.ok(new GenericEntity<List<ActorDto>>(actorDtoList) {}).build();
            } else {
                // Si no se proporcionan parámetros válidos
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("You must provide either 'mediaId' or 'search' as query parameters.")
                        .build();
            }
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the request.")
                    .build();
        }

    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_ACTOR)
    public Response getActor(@PathParam("id") @NotNull final int id) {
        try {
            Actor actor=actorService.getActorById(id);
            return Response.ok(ActorDto.fromActor(actor, uriInfo)).build();
        }catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Actor not found.")
                    .build();
        }
        catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the request.")
                    .build();
        }
    }

}

