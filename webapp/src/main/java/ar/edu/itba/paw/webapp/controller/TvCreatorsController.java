package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.TV.TVCreators;
import ar.edu.itba.paw.services.TVCreatorsService;
import ar.edu.itba.paw.webapp.dto.out.TvCreatorsDto;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("tvCreators")
@Component
public class TvCreatorsController {

    private final TVCreatorsService tvCreatorsService;


    @Context
    UriInfo uriInfo;

    @Autowired
    public TvCreatorsController(TVCreatorsService tvCreatorsService) {
        this.tvCreatorsService = tvCreatorsService;
    }


    @GET
    @Produces(VndType.APPLICATION_TVCREATOR_LIST)
    public Response getTVCreators(
            @QueryParam("search") final String search,
            @QueryParam("mediaId") final Integer mediaId
    ) {
        try {
            if (search != null && !search.isEmpty()) {
                // Lógica para obtener creadores de TV por consulta de búsqueda
                List<TVCreators> tvCreatorsList = tvCreatorsService.getTVCreatorsForQuery(search, 10);
                List<TvCreatorsDto> tvCreatorsDtoList = TvCreatorsDto.fromTvCreatorList(tvCreatorsList, uriInfo);
                return Response.ok(new GenericEntity<List<TvCreatorsDto>>(tvCreatorsDtoList) {}).build();
            } else if (mediaId != null) {
                // Lógica para obtener creadores de TV por ID de medio
                List<TVCreators> tvCreators = tvCreatorsService.getTvCreatorsByMediaId(mediaId);
                List<TvCreatorsDto> tvCreatorsDtos = TvCreatorsDto.fromTvCreatorList(tvCreators, uriInfo);
                return Response.ok(new GenericEntity<List<TvCreatorsDto>>(tvCreatorsDtos) {}).build();
            }

            // Si no se proporcionan parámetros válidos
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("You must provide either 'search' or 'mediaId' as query parameters.")
                    .build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the request.")
                    .build();
        }

    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_TVCREATOR)
    public Response getTvCreatorById(@PathParam("id") @NotNull final int tvCreatorId) {
        try {
            TVCreators tvCreators=tvCreatorsService.getTvCreatorById(tvCreatorId);
            return Response.ok(TvCreatorsDto.fromTvCreator(tvCreators,uriInfo)).build();
        }catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("TV Creator with id " + tvCreatorId + " not found.")
                    .build();
        }
        catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the request.")
                    .build();
        }
    }
}

