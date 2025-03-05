package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;

import ar.edu.itba.paw.exceptions.ForbiddenException;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.services.MoovieListService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.webapp.dto.in.MoovieListReviewCreateDto;
import ar.edu.itba.paw.webapp.dto.out.MoovieListReviewDto;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("moovieListReviews")
@Component
public class MoovieListReviewController {
    private final ReviewService reviewService;
    private final MoovieListService moovieListService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MoovieListReviewController(ReviewService reviewService, MoovieListService moovieListService) {
        this.reviewService = reviewService;
        this.moovieListService = moovieListService;
    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_MOOVIELIST_REVIEW)
    public Response getMoovieListReviewById(@PathParam("id") @NotNull int id) {
        try {
            final MoovieListReview moovieListReview = reviewService.getMoovieListReviewById(id);
            final MoovieListReviewDto moovieListReviewDto = MoovieListReviewDto.fromMoovieListReview(moovieListReview, uriInfo);
            return Response.ok(moovieListReviewDto).build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList review not found.\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(VndType.APPLICATION_MOOVIELIST_REVIEW_LIST)
    public Response getMoovieListReviewsFromQueryParams(
            @QueryParam("listId") final Integer listId,
            @QueryParam("userId") final Integer userId,
            @QueryParam("pageNumber") @DefaultValue("1") final int page) {

        try {
            if (listId != null) {
                // Get MoovieList if it exists
                moovieListService.getMoovieListById(listId);
                final List<MoovieListReview> moovieListReviews = reviewService.getMoovieListReviewsByMoovieListId(
                        listId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
                final int moovieListReviewsCount = reviewService.getMoovieListReviewByMoovieListIdCount(listId);
                final List<MoovieListReviewDto> moovieListReviewDtos = MoovieListReviewDto.fromMoovieListReviewList(moovieListReviews, uriInfo);
                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MoovieListReviewDto>>(moovieListReviewDtos) {
                });
                final PagingUtils<MoovieListReview> toReturnMoovieListReviews = new PagingUtils<>(moovieListReviews, page - 1, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), moovieListReviewsCount);
                ResponseUtils.setPaginationLinks(res, toReturnMoovieListReviews, uriInfo);
                return res.build();

            } else if (userId != null) {
                final List<MoovieListReview> moovieListReviews = reviewService.getMoovieListReviewsFromUser(
                        userId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
                final List<MoovieListReviewDto> moovieListReviewDtos = MoovieListReviewDto.fromMoovieListReviewList(moovieListReviews, uriInfo);
                return Response.ok(new GenericEntity<List<MoovieListReviewDto>>(moovieListReviewDtos) {
                }).build();

            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Either listId or userId must be provided.").build();
            }
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList not found.\"}")
                    .build();
        }
        catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).
                    entity("{\"error\":\"User not found.\"}").
                    build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PUT
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes({VndType.APPLICATION_MOOVIELIST_REVIEW_FORM})
    @Produces(MediaType.APPLICATION_JSON)
    public Response editReview(@QueryParam("listId") @NotNull int listId,
                               @Valid @NotNull final MoovieListReviewCreateDto moovieListReviewDto) {
        try {
            moovieListService.getMoovieListById(listId);
            reviewService.editReview(
                    listId,
                    0,
                    moovieListReviewDto.getReviewContent(),
                    ReviewTypes.REVIEW_MOOVIE_LIST
            );
            return Response.ok()
                    .entity("MoovieList review successfully updated for MoovieList with ID: " + listId)
                    .build();
        }catch (MoovieListNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList not found.\"}")
                    .build();
        }catch (ReviewNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Review not found.\"}")
                    .build();
        } catch (ForbiddenException e){
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"You do not have permission, list is private.\"}")
                    .build();
        }
        catch (RuntimeException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes({VndType.APPLICATION_MOOVIELIST_REVIEW_FORM})
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMoovieListReview(@QueryParam("listId") @NotNull int listId,
                                                            @Valid @NotNull final MoovieListReviewCreateDto moovieListReviewDto) {
        try {
            moovieListService.getMoovieListById(listId);

            reviewService.createReview(
                    listId,
                    0,
                    moovieListReviewDto.getReviewContent(),
                    ReviewTypes.REVIEW_MOOVIE_LIST
            );

            return Response.status(Response.Status.CREATED)
                    .entity("MoovieList review successfully created to the list with ID: " + listId)
                    .build();
        }catch (ForbiddenException e){
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"You do not have permission to create a review for this MoovieList.\"}")
                    .build();
        }
        catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList not found.\"}")
                    .build();
        }catch (ReviewAlreadyCreatedException e){
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Review already created for this MoovieList.\"}")
                    .build();
        }
        catch (RuntimeException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }


    @PUT
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response feedbackMoovieListReview(@PathParam("id") @NotNull final int id) {
        try {
            boolean liked_status=reviewService.likeReview(id, ReviewTypes.REVIEW_MOOVIE_LIST);

            if(liked_status){
                return Response.ok()
                    .entity("MoovieList review feedback status successfully changed to liked.")
                    .build();
            }else{
                return Response.ok()
                    .entity("MoovieList review feedback status successfully changed to unliked.")
                        .build();
            }

        } catch (UserNotLoggedException | UnableToFindUserException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to like a review.\"}")
                    .build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList review not found.\"}")
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList review not found.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMoovieListReviewById(@PathParam("id") final int id) {
        try {
            reviewService.deleteReview(id, ReviewTypes.REVIEW_MOOVIE_LIST);

            return Response.ok()
                    .entity("Review successfully deleted.")
                    .build();

        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to delete a review.\"}")
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList review not found.\"}")
                    .build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"You do not have permission to delete this review.\"}")
                    .build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }




}






