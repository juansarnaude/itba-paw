package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.Comments.CommentFeedbackType;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.services.CommentService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.webapp.dto.in.CommentCreateDto;
import ar.edu.itba.paw.webapp.dto.in.CommentFeedbackDto;
import ar.edu.itba.paw.webapp.dto.out.CommentDto;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("comments")
@Component
public class CommentController {
    private final CommentService commentService;
    private final ReviewService reviewService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public CommentController(CommentService commentService, ReviewService reviewService) {
        this.commentService = commentService;
        this.reviewService = reviewService;
    }

    @GET
    @Produces(VndType.APPLICATION_COMMENT_LIST)
    public Response getCommentsByReviewId(@QueryParam("reviewId") @NotNull final int reviewId, @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        try {
            final int commentCount = reviewService.getReviewById(reviewId).getCommentCount().intValue();
            final List<Comment> commentList = commentService.getComments(reviewId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
            final List<CommentDto> commentDtoList = CommentDto.fromCommentList(commentList, uriInfo);

            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<CommentDto>>(commentDtoList){} );
            final PagingUtils<Comment> reviewPagingUtils = new PagingUtils<>(commentList, page, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), commentCount);
            ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
            return res.build();
        }catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Review not found")
                    .build();
        }
        catch (RuntimeException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_COMMENT)
    public Response getCommentById(@PathParam("id") @NotNull int id) {
        try {
            final Comment comment = commentService.getCommentById(id);
            if (comment == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Comment not found")
                        .build();
            }
            final CommentDto commentDto = CommentDto.fromComment(comment, uriInfo);
            return Response.ok(commentDto).build();
        }
        catch (RuntimeException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }




    @POST
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({VndType.APPLICATION_COMMENT_FORM})
    public Response createComment(@QueryParam("reviewId") @NotNull final int reviewId, @Valid @NotNull final CommentCreateDto commentDto) {
        try {
            commentService.createComment(
                    reviewId,
                    commentDto.getCommentContent()
            );
            return Response.status(Response.Status.CREATED)
                    .entity("Comment successfully created to review with id:" + reviewId)
                    .build();
        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to create a comment.\"}")
                    .build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Review not found")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes({VndType.APPLICATION_COMMENT_FEEDBACK_FORM})
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFeedbackOnComment(@PathParam("id") @NotNull int id, @Valid @NotNull final CommentFeedbackDto commentFeedbackDto) {
        try {
            Comment comment = commentService.getCommentById(id);
            if(comment == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Comment not found")
                        .build();
            }
            CommentFeedbackType commentFeedbackType=commentFeedbackDto.transformToEnum();
            if (commentFeedbackType == CommentFeedbackType.LIKE) {
                boolean liked = commentService.likeComment(id);
                if (!liked) {
                    return Response.ok()
                            .entity("Comment feedback status successfully changed to unliked")
                            .build();
                }
                else {
                    return Response.ok()
                            .entity("Comment feedback status successfully changed to liked")
                            .build();
                }
            } else if (commentFeedbackType == CommentFeedbackType.DISLIKE) {
                boolean disliked=commentService.dislikeComment(id);
                if (!disliked) {
                    return Response.ok()
                            .entity("Comment feedback status successfully changed to undisliked")
                            .build();
                }
                else {
                    return Response.ok()
                            .entity("Comment feedback status successfully changed to disliked")
                            .build();
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid feedback type")
                        .build();
            }
        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to update a comment.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }

    }


    @DELETE
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserLoggedIn() or @accessValidator.isUserAdmin()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteComment(@PathParam("id") @NotNull int id) {
            try {
                if (commentService.getCommentById(id) == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Comment not found")
                            .build();
                }
                commentService.deleteComment(id);
                return Response.ok().entity("Comment deleted").build();
            }catch (AccessDeniedException e) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"error\":\"User is not the author of the comment.\"}")
                        .build();
            }
            catch (UserNotLoggedException e) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"User must be logged in to delete a comment.\"}")
                        .build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An unexpected error occurred: " + e.getMessage())
                        .build();
            }
        }


}
