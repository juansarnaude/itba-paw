package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.exceptions.MediaNotFoundException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.services.*;

import ar.edu.itba.paw.webapp.dto.out.MediaDto;
import ar.edu.itba.paw.webapp.dto.out.MovieDto;
import ar.edu.itba.paw.webapp.dto.out.TVSerieDto;

import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

@Path("medias")
@Component
public class MediaController {

    private final MediaService mediaService;
    private final TVCreatorsService tvCreatorsService;
    private final ActorService actorService;
    private final MoovieListService moovieListService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MediaController(MediaService mediaService,TVCreatorsService tvCreatorsService, ActorService actorService, MoovieListService moovieListService) {
        this.mediaService = mediaService;
        this.tvCreatorsService= tvCreatorsService;
        this.actorService = actorService;
        this.moovieListService = moovieListService;
    }




    @GET
    @Produces(VndType.APPLICATION_MEDIA_LIST)
    public Response getMedia(
            @QueryParam("type") @DefaultValue("-1") final int type,
            @QueryParam("pageNumber") @DefaultValue("1") final int page,
            @QueryParam("pageSize") @DefaultValue("-1") final int pageSize,
            @QueryParam("orderBy") final String orderBy,
            @QueryParam("sortOrder") final String sortOrder,
            @QueryParam("search") final String search,
            @QueryParam("providers") final List<Integer> providers,
            @QueryParam("genres") final List<Integer> genres,
            @QueryParam("ids") final String ids,
            @QueryParam("tvCreatorId") final Integer tvCreatorId,
            @QueryParam("directorId") final Integer directorId,
            @QueryParam("actorId") final Integer actorId,
            @QueryParam("moovieListId") final Integer moovieListId
    ) {
        try {
            if (moovieListId != null) {
                // Obtener recomendaciones de medios para agregar a una lista de películas
                List<MediaDto> mlcList = MediaDto.fromMediaList(
                        moovieListService.getRecommendedMediaToAdd(moovieListId, 5), uriInfo);
                return Response.ok(new GenericEntity<List<MediaDto>>(mlcList) {}).build();
            }

            if (ids != null && !ids.isEmpty()) {
                // Lógica para manejar la lista de IDs
                if (ids.length() > 100) {
                    throw new IllegalArgumentException("Invalid ids, param. A comma separated list of Media IDs. Up to 100 are allowed in a single request.");
                }

                List<Integer> idList = new ArrayList<>();
                String[] splitIds = ids.split(",");
                for (String id : splitIds) {
                    try {
                        idList.add(Integer.parseInt(id.trim()));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid ids, param. A comma separated list of Media IDs. Up to 100 are allowed in a single request.");
                    }
                }

                if (idList.size() > 100 || idList.isEmpty()) {
                    throw new IllegalArgumentException("Invalid ids, param. A comma separated list of Media IDs. Up to 100 are allowed in a single request.");
                }

                List<MediaDto> mediaList = new ArrayList<>();
                for (int id : idList) {
                    Media media = mediaService.getMediaById(id);
                    if (media.isType()) {
                        mediaList.add(TVSerieDto.fromTVSerie(mediaService.getTvById(id), uriInfo));
                    } else {
                        mediaList.add(MovieDto.fromMovie(mediaService.getMovieById(id), uriInfo));
                    }
                }
                return Response.ok(new GenericEntity<List<MediaDto>>(mediaList) {}).build();

            } else if (tvCreatorId != null) {
                // Lógica para manejar el tvCreatorId
                List<Media> mediaList = tvCreatorsService.getMediasForTVCreator(tvCreatorId);

                if (mediaList == null || mediaList.isEmpty()) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("No media found for TV creator with ID: " + tvCreatorId)
                            .build();
                }

                List<MediaDto> mediaDtos = MediaDto.fromMediaList(mediaList, uriInfo);
                return Response.ok(new GenericEntity<List<MediaDto>>(mediaDtos) {}).build();

            } else if (directorId != null) {
                // Lógica para manejar el directorId
                List<Movie> mediaList = mediaService.getMediaForDirectorId(directorId);

                if (mediaList == null || mediaList.isEmpty()) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("No media found for director with ID: " + directorId)
                            .build();
                }

                List<MovieDto> mediaDtos = MovieDto.fromMovieList(mediaList, uriInfo);
                return Response.ok(new GenericEntity<List<MovieDto>>(mediaDtos) {}).build();

            } else if (actorId != null) {
                // Lógica para manejar el actorId
                List<Media> mediaList = actorService.getMediaForActor(actorId);

                if (mediaList == null || mediaList.isEmpty()) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("No media found for actor with ID: " + actorId)
                            .build();
                }

                List<MediaDto> mediaDtos = MediaDto.fromMediaList(mediaList, uriInfo);
                return Response.ok(new GenericEntity<List<MediaDto>>(mediaDtos) {}).build();

            } else {
                // Lógica para los parámetros tipo, búsqueda, paginación, etc.
                int typeQuery = MediaTypes.TYPE_ALL.getType();
                if (type == MediaTypes.TYPE_MOVIE.getType() || type == MediaTypes.TYPE_TVSERIE.getType()) {
                    typeQuery = type;
                }

                int pageSizeQuery = pageSize;
                if (pageSize < 1 || pageSize > PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize()) {
                    pageSizeQuery = PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize();
                }

                List<Media> mediaList = mediaService.getMedia(typeQuery, search, null,
                        genres, providers, null, null, orderBy, sortOrder, pageSizeQuery, page - 1);

                final int mediaCount = mediaService.getMediaCount(typeQuery, search, null,
                        genres, providers, null, null);

                List<MediaDto> mediaDtoList = MediaDto.fromMediaList(mediaList, uriInfo);
                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MediaDto>>(mediaDtoList) {});
                final PagingUtils<Media> toReturnMediaList = new PagingUtils<>(mediaList, page - 1, pageSizeQuery, mediaCount);
                ResponseUtils.setPaginationLinks(res, toReturnMediaList, uriInfo);
                return res.build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_MEDIA)
    public Response getMediaById(@PathParam("id") final int id) {
        try {
            Media media = mediaService.getMediaById(id);

            if (media == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Media with ID: " + id + " not found.")
                        .build();
            }

            if(media.isType()){
                return Response.ok(TVSerieDto.fromTVSerie(mediaService.getTvById(id), uriInfo)).build();
            }
            return Response.ok(MovieDto.fromMovie(mediaService.getMovieById(id), uriInfo)).build();
        }catch (MediaNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Media with ID: " + id + " not found.")
                    .build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred: " + e.getMessage())
                    .build();
        }


    }


}
