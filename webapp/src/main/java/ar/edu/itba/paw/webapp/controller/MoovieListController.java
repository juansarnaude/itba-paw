package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.ForbiddenException;
import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.Media.OrderedMedia;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;

import ar.edu.itba.paw.services.MoovieListService;


import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.in.*;

import ar.edu.itba.paw.webapp.dto.out.MediaIdListIdDto;
import ar.edu.itba.paw.webapp.dto.out.MoovieListDto;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;

import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

@Path("lists")
@Component
public class MoovieListController {

    private final MoovieListService moovieListService;
    private final UserService userService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MoovieListController(MoovieListService moovieListService, UserService userService) {
        this.moovieListService = moovieListService;
        this.userService = userService;
    }



    @GET
    @Produces(VndType.APPLICATION_MOOVIELIST_LIST)
    public Response getMoovieList(
            @QueryParam("ids") final String ids,
            @QueryParam("search") String search,
            @QueryParam("ownerUsername") String ownerUsername,
            @QueryParam("type") @DefaultValue("-1") int type,
            @QueryParam("orderBy") String orderBy,
            @QueryParam("order") String order,
            @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber
    ) {
        try {
            // Buscar por IDs si se proporcionan
            if (ids != null && !ids.isEmpty()) {
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

                if (idList.size() >= 25 || idList.size() < 0) {
                    throw new IllegalArgumentException("Invalid ids, param. A comma separated list of Media IDs. Up to 100 are allowed in a single request.");
                }

                List<MoovieListDto> mlList = new ArrayList<>();
                for (int id : idList) {
                    try{
                        MoovieListDto mlc = MoovieListDto.fromMoovieList(moovieListService.getMoovieListCardById(id), uriInfo);
                        mlList.add(mlc);
                    } catch ( InvalidAccessToResourceException e){
                        //We just ignored the private list its trying to access
                    }
                }

                return Response.ok(new GenericEntity<List<MoovieListDto>>(mlList) {}).build();
            }
            // Buscar por otros criterios si no se proporcionan IDs
            else {
                if (type < 1 || type > 4) {
                    type = MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType();
                }

                List<MoovieListCard> moovieListCardList = moovieListService.getMoovieListCards(search, ownerUsername, type, orderBy, order, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), pageNumber);
                final int moovieListCardCount = moovieListService.getMoovieListCardsCount(search, ownerUsername, type);

                List<MoovieListDto> mlcDto = MoovieListDto.fromMoovieListList(moovieListCardList, uriInfo);

                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MoovieListDto>>(mlcDto) {});
                final PagingUtils<MoovieListCard> toReturnMoovieListCardList = new PagingUtils<>(moovieListCardList, pageNumber, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), moovieListCardCount);
                ResponseUtils.setPaginationLinks(res, toReturnMoovieListCardList, uriInfo);

                return res.build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_MOOVIELIST_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMoovieList(@Valid final MoovieListCreateDto listDto) {
        try {



            MoovieListTypes moovieListType = MoovieListTypes.fromType(listDto.getType());

            int listId = moovieListService.createMoovieList(
                    listDto.getName(),
                    moovieListType.getType(),
                    listDto.getDescription()
            ).getMoovieListId();
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(String.valueOf(listId));

            return Response.created(uriBuilder.build())
                    .entity("{\"message\":\"Movie list created successfully.\", \"url\": \"" + uriBuilder.build().toString() + "\"}")
                    .build();


        }
        catch (UnableToInsertIntoDatabase | DuplicateKeyException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("A movie list with the same name already exists.")
                    .build();
        }
        catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }


    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_MOOVIELIST)
    public Response getMoovieListById(@PathParam("id") final int id) {
        try {
            MoovieListCard list=moovieListService.getMoovieListCardById(id);
            return Response.ok(MoovieListDto.fromMoovieList(list, uriInfo)).build();
        }catch (InvalidAccessToResourceException e){
            return Response.status(Response.Status.FORBIDDEN).entity(new ResponseMessage("You do not have access to this resource.")).build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessage("MoovieList not found.")).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ResponseMessage("An unexpected error occurred.")).build();
        }

    }

    @PUT
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_MOOVIELIST_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editMoovieList(@PathParam("id") @NotNull int listId,
                                   @Valid @NotNull final EditListDTO editListForm) {
        try {
            moovieListService.editMoovieList(listId, editListForm.getListName(), editListForm.getListDescription());

            return Response.ok()
                    .entity("MoovieList successfully edited for MoovieList with ID: " + listId)
                    .build();
        }catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("MoovieList not found.")
                    .build();
        }
        catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }

    }

    @PUT
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_MOOVIELIST_FEEDBACK_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response moovieListFeedback(@PathParam("id") int id,
                                       @Valid MoovieListFeedbackDto moovieListFeedbackDto) {
        try {
            userService.isUsernameMe(moovieListFeedbackDto.getUsername());
            if(moovieListFeedbackDto.getFeedbackType().equals("LIKE")) {
                boolean like = moovieListService.likeMoovieList(id);
                if (like) {
                    return Response.ok()
                            .entity("{\"message\":\"Succesfully liked list.\"}").build();
                }
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\":\"List is already liked.\"}").build();
            } else if(moovieListFeedbackDto.getFeedbackType().equals("UNLIKE")) {
                boolean unlike = moovieListService.removeLikeMoovieList(id);
                if (unlike) {
                    return Response.ok()
                            .entity("{\"message\":\"Succesfully unliked list.\"}").build();
                }
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\":\"List is not liked.\"}").build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Invalid feedback type.\"}")
                    .build();
        }
        catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        }
        catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"An unexpected error occurred.\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_MOOVIELIST_FOLLOW_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response followMoovieList(@PathParam("id") int id,
                                     @Valid @NotNull FollowMoovieListDto followMoovieListDto) {
        try {

            userService.isUsernameMe(followMoovieListDto.getUsername());
            if (followMoovieListDto.getActionType().equals("FOLLOW")) {
                boolean followed = moovieListService.followMoovieList(id);
                if (followed) {
                    return Response.ok()
                            .entity("{\"message\":\"Succesfully followed list.\"}").build();
                }
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\":\"List is already followed.\"}").build();
            } else if (followMoovieListDto.getActionType().equals("UNFOLLOW")) {
                boolean unfollowed = moovieListService.removeFollowMoovieList(id);
                if (unfollowed) {
                    moovieListService.removeFollowMoovieList(id);
                    return Response.ok()
                            .entity("{\"message\":\"Succesfully unfollowed list.\"}").build();
                }
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\":\"List is not followed.\"}").build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Invalid action type.\"}")
                    .build();
        }catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        }
        catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"An unexpected error occurred.\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMoovieList(@PathParam("id") final int id) {
        try {
            moovieListService.deleteMoovieList(id);
            return Response.ok().build();
        }
        catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("MoovieList not found.")
                    .build();
        }
        catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("You do not have access to this resource.")
                    .build();
        }
        catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }

    }


    @GET
    @Path("{id}/recommendedLists")
    @Produces(VndType.APPLICATION_MOOVIELIST_LIST)
    public Response getRecommendedLists(@PathParam("id") final int id) {
        try {
            List<MoovieListDto> mlcList = MoovieListDto.fromMoovieListList(moovieListService.getRecommendedMoovieListCards(id, 4, 0), uriInfo);
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MoovieListDto>>(mlcList) {
            });
            return res.build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ResponseMessage("An unexpected error occurred.")).build();
        }
    }

    //We have a separate endpoint for content to be able to use filters and no need to do it every time we want to find a list
    // PROBLEM WHEN SORT ORDER AND OR ORDER BY ARE NULL
    @GET
    @Path("/{id}/content")
    @Produces(VndType.APPLICATION_MOOVIELIST_MEDIA_LIST)
    public Response getMoovieListMedia(@PathParam("id") final int id,
                                                        @QueryParam("orderBy") @DefaultValue("customOrder") final String orderBy,
                                                        @QueryParam("sortOrder") @DefaultValue("DESC") final String sortOrder,
                                                        @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                                                        @QueryParam("pageSize") @DefaultValue("-1") final int pageSize) {
        try {
            moovieListService.getMoovieListCardById(id);
            int pageSizeQuery = pageSize;
            if (pageSize < 1 || pageSize > PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize()) {
                pageSizeQuery = PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize();
            }

            List<OrderedMedia> mediaList = moovieListService.getMoovieListContentOrdered(id, orderBy, sortOrder, pageSizeQuery, pageNumber);
            final int mediaCount = moovieListService.getMoovieListCardById(id).getSize();
            List<MediaIdListIdDto> dtoList = MediaIdListIdDto.fromOrderedMediaList(mediaList, id, uriInfo);
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MediaIdListIdDto>>(dtoList) {
            });
            final PagingUtils<MediaIdListIdDto> toReturnMediaIdListId = new PagingUtils<>(dtoList, pageNumber, pageSizeQuery, mediaCount);
            ResponseUtils.setPaginationLinks(res, toReturnMediaIdListId, uriInfo);
            return res.build();
        }catch (InvalidAccessToResourceException e){
            return Response.status(Response.Status.FORBIDDEN).entity(new ResponseMessage("You do not have access to this resource.")).build();
        }
        catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ResponseMessage("An unexpected error occurred.")).build();
        }

    }

    @POST
    @Path("/{id}/content")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_MOOVIELIST_MEDIA_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertMediaIntoMoovieList(@PathParam("id") int id,
                                              @Valid MediaListDto mediaIdListDto) {

        try {
            moovieListService.getMoovieListCardById(id);
            List<Integer> mediaIdList = mediaIdListDto.getMediaIdList();
            MoovieList updatedList = moovieListService.insertMediaIntoMoovieList(id, mediaIdList);
            if (mediaIdList == null || mediaIdList.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ResponseMessage("No media IDs provided."))
                        .build();
            }
            return Response.ok(updatedList).entity(new ResponseMessage("Media added successfully to the list.")).build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ResponseMessage("You do not have access to this resource."))
                    .build();
        }
        catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ResponseMessage("MoovieList not found."))
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ResponseMessage("An unexpected error occurred."))
                    .build();
        }

    }


    @GET
    @Path("/{id}/content/{mediaId}")
    @Produces(VndType.APPLICATION_MOOVIELIST_MEDIA)
    public Response getMoovieListMediaByMediaId(@PathParam("id") final int id,
                                                @PathParam("mediaId") final int mediaId ){
        try {
            moovieListService.getMoovieListCardById(id);

            int customOrder = moovieListService.isMediaInMoovieList(mediaId,id);
            if( customOrder == -1){
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ResponseMessage("Media not found in MoovieList."))
                        .build();
            }
            return Response.ok(new MediaIdListIdDto(mediaId, id, customOrder, uriInfo)).build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ResponseMessage("You do not have access to this resource."))
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ResponseMessage("MoovieList not found."))
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ResponseMessage("An unexpected error occurred."))
                    .build();
        }

    }

    @PUT
    @Path("/{id}/content/{mediaId}")
    @PreAuthorize("@accessValidator.isUserListAuthor(#id)")
    @Consumes(VndType.APPLICATION_MOOVIELIST_MEDIA_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editMoovieListMediaByMediaId(@PathParam("id") final int id,
                                                @PathParam("mediaId") final int mediaId,
                                                 final MediaIdListIdDto input){
        try {
            moovieListService.getMoovieListCardById(id);

            moovieListService.updateMoovieListOrder(input.getMoovieListId(), input.getMediaId(), input.getCustomOrder());
            return Response.ok()
                    .entity("MoovieList order succesfully modified.").build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ResponseMessage("You do not have access to this resource."))
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ResponseMessage("MoovieList not found."))
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ResponseMessage("An unexpected error occurred."))
                    .build();
        }

    }

    @DELETE
    @Path("/{id}/content/{mediaId}")
    @PreAuthorize("@accessValidator.isUserListAuthor(#id)")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMediaMoovieList(@PathParam("id") final int id, @PathParam("mediaId") final int mId) {
        try {
            moovieListService.deleteMediaFromMoovieList(id, mId);
            return Response.noContent().build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ResponseMessage("You do not have access to this resource."))
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ResponseMessage("MoovieList not found."))
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ResponseMessage("An unexpected error occurred."))
                    .build();
        }

    }



}
