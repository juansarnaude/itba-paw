package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.NoFileException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.MoovieList.UserMoovieListId;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.dto.in.JustIdDto;
import ar.edu.itba.paw.webapp.dto.out.*;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

@Path("/profiles")
@Component
public class ProfileController {

    private static int DEFAULT_PAGE_INT = 1;
    private final UserService userService;
    private final MoovieListService moovieListService;
    private final MediaService mediaService;
    private final ReviewService reviewService;
    private final CommentService commentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);


    @Context
    private UriInfo uriInfo;

    @Autowired
    public ProfileController(UserService userService, MoovieListService moovieListService, MediaService mediaService, ReviewService reviewService, CommentService commentService) {
        this.userService = userService;
        this.moovieListService = moovieListService;
        this.mediaService = mediaService;
        this.reviewService = reviewService;
        this.commentService = commentService;
    }

    @GET
    @Produces(VndType.APPLICATION_PROFILE_LIST)
    public Response searchProfiles(@QueryParam("username") final String username,
                                   @QueryParam("orderBy") final String orderBy,
                                   @QueryParam("sortOrder") final String sortOrder,
                                   @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        try {
            List<Profile> profileList = userService.searchUsers(username, orderBy, sortOrder, PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), pageNumber);
            final int profileCount = userService.getSearchCount(username);
            List<ProfileDto> profileDtoList = ProfileDto.fromProfileList(profileList, uriInfo);
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ProfileDto>>(profileDtoList) {
            });
            final PagingUtils<Profile> toReturnProfileList = new PagingUtils<>(profileList, pageNumber, PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), profileCount);
            ResponseUtils.setPaginationLinks(res, toReturnProfileList, uriInfo);
            return res.build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(VndType.APPLICATION_PROFILE_LIST)
    @Path("/milkyLeaderboard")
    public Response getMilkyLeaderboard(@QueryParam("page") @DefaultValue("1") final int page,
                                        @QueryParam("pageSize") @DefaultValue("-1") final int pageSize) {
        LOGGER.info("Method: getMilkyLeaderboard, Path: /users/milkyLeaderboard, Page: {}, PageSize: {}", page, pageSize);
        try {
            int pageSizeQuery = pageSize;
            if (pageSize < 1 || pageSize > PagingSizes.MILKY_LEADERBOARD_DEFAULT_PAGE_SIZE.getSize()) {
                pageSizeQuery = PagingSizes.MILKY_LEADERBOARD_DEFAULT_PAGE_SIZE.getSize();
            }
            List<ProfileDto> leaderboards = ProfileDto.fromProfileList(userService.getMilkyPointsLeaders(pageSizeQuery, page), uriInfo);
            return Response.ok(new GenericEntity<List<ProfileDto>>(leaderboards) {
            }).build();
        } catch (RuntimeException e) {
            LOGGER.error("Error retrieving milky leaderboard: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{username}")
    @Produces(VndType.APPLICATION_PROFILE)
    public Response getProfileByUsername(@PathParam("username") final String username) {
        LOGGER.info("Method: getProfileByUsername, Path: /users/profile/{username}, Username: {}", username);
        try {
            final Profile profile = userService.getProfileByUsername(username);
            return Response.ok(ProfileDto.fromProfile(profile, uriInfo)).build();
        } catch (UnableToFindUserException e) {
            LOGGER.error("Error retrieving profile: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        catch (RuntimeException e) {
            LOGGER.error("Error retrieving profile: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{username}/image")
    @Produces("image/png")
    public Response getProfileImage(@PathParam("username") final String username) {
        try {
            LOGGER.info("Method: getProfileImage, Path: /users/{username}/image, Username: {}", username);
            final byte[] image = userService.getProfilePicture(username);
            return Response.ok(image).build();
        }catch (NoFileException | UnableToFindUserException e) {
            LOGGER.error("Error retrieving profile image: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        catch (RuntimeException e) {
            LOGGER.error("Error retrieving profile image: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{username}/image")
    @PreAuthorize("@accessValidator.checkIsUserMe(#username)")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfileImage(@PathParam("username") String username,
                                       @FormDataParam("image") final FormDataBodyPart image,
                                       @Size(max = 1024 * 1024 * 2) @FormDataParam("image") byte[] imageBytes) {
        try {
            LOGGER.info("Method: setProfileImage, Path: /users/{username}/image, Username: {}", username);

            userService.setProfilePicture(imageBytes, image.getMediaType().getSubtype());

            LOGGER.info("Profile image updated for username: {}", username);
            return Response.ok().build();
        } catch (UnableToFindUserException e) {
            LOGGER.error("Error updating profile image: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            LOGGER.error("Error updating profile image: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }


    /***
     * Watched
     */

    @GET
    @Path("/{username}/watched")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(VndType.APPLICATION_WATCHED_LIST)
    public Response getWatched(@PathParam("username") final String username,
                               @QueryParam("orderBy") String orderBy,
                               @QueryParam("order") String order,
                               @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {


        try {
            userService.getProfileByUsername(username);

            MoovieListCard ml = moovieListService.getMoovieListCards("Watched", username,
                    MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),
                    null, null, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(), 1).get(0);

            List<Media> mediaList = moovieListService.getMoovieListContent(ml.getMoovieListId(),orderBy,order,PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber);

            int mediaCount = ml.getSize();
            List<MediaIdDto> listToRet = new ArrayList<>();

            for ( Media media : mediaList){
                listToRet.add(new MediaIdDto(media.getMediaId(), username, uriInfo));
            }

            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MediaIdDto>>(listToRet) {
            });

            final PagingUtils<Media> toReturnMoovieListCardList = new PagingUtils<>(mediaList, pageNumber, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), mediaCount);
            ResponseUtils.setPaginationLinks(res, toReturnMoovieListCardList, uriInfo);
            return res.build();
        }
        catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
        catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }

    @POST
    @Path("/{username}/watched")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_WATCHED_MEDIA_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertIntoWatched(@PathParam("username") final String username,
                                      @Valid final JustIdDto justIdDto){
        try {
            moovieListService.addMediaToWatched(justIdDto.getId(), username);
            return Response.ok().build();
        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }


    @GET
    @Path("/{username}/watched/{mediaId}")
    @PreAuthorize("@accessValidator.checkIsUserMe(#username)")
    @Produces(VndType.APPLICATION_WATCHED_MEDIA)
    public Response getWatchedMediaByMediaId(@PathParam("username") final String username,
                                             @PathParam("mediaId") final int mediaId) {
        try {
            userService.isUsernameMe(username);
            boolean watched = mediaService.getMediaById(mediaId).isWatched();
            if(watched){
                return Response.ok(new MediaIdDto(mediaId, username, uriInfo)).build();
            }
            return Response.noContent().build();
        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }

    @DELETE
    @Path("/{username}/watched/{mediaId}")
    @PreAuthorize("@accessValidator.checkIsUserMe(#username)")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFromWatched(@PathParam("username") final String username,
                                      @PathParam("mediaId") final int mediaId){
        try {
            moovieListService.removeMediaFromWatched(mediaId, username);
            return Response.ok().build();
        } catch (UnableToFindUserException | NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }


    @GET
    @Path("/{username}/watched/count")
    @PreAuthorize("@accessValidator.checkIsUserMe(#username)")
    @Produces(VndType.APPLICATION_WATCHLIST_COUNT)
    public Response getWatchedAmountMediaByListId(@PathParam("username") final String username,
                                             @QueryParam("listId") @NotNull final int listId) {
        try {

            MoovieListCard card = moovieListService.getMoovieListCardById(listId);
            if (card == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("List not found").build();
            }
            int count = card.getCurrentUserWatchAmount();
            return Response.ok(CountDto.fromCount(count)).build();
        }
        catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
        catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }

    /***
     * Watchlist
     */
    @GET
    @Path("/{username}/watchlist")
    @PreAuthorize("@accessValidator.checkIsUserMe(#username)")
    @Produces(VndType.APPLICATION_WATCHLIST_LIST)
    public Response getWatchlist(@PathParam("username") final String username,
                                 @QueryParam("orderBy") String orderBy,
                                 @QueryParam("order") String order,
                                 @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {

        try {

            userService.getProfileByUsername(username);

            MoovieListCard ml = moovieListService.getMoovieListCards("Watchlist", username,
                    MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),
                    null, null, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(), 1).get(0);

            List<Media> mediaList = moovieListService.getMoovieListContent(ml.getMoovieListId(),orderBy,order,PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber);

            int mediaCount = ml.getSize();
            List<MediaIdDto> listToRet = new ArrayList<>();

            for ( Media media : mediaList){
                listToRet.add(new MediaIdDto(media.getMediaId(), username, uriInfo));
            }

            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MediaIdDto>>(listToRet) {
            });

            final PagingUtils<Media> toReturnMoovieListCardList = new PagingUtils<>(mediaList, pageNumber, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), mediaCount);
            ResponseUtils.setPaginationLinks(res, toReturnMoovieListCardList, uriInfo);
            return res.build();
        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }

    @POST
    @Path("/{username}/watchlist")
    @PreAuthorize("@accessValidator.checkIsUserMe(#username)")
    @Consumes(VndType.APPLICATION_WATCHLIST_MEDIA_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertIntoWatchlist(@PathParam("username") final String username,
                                        @Valid  final JustIdDto justIdDto){
        try {
            moovieListService.addMediaToWatchlist(justIdDto.getId(), username);
            return Response.ok().build();
        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }

    @GET
    @Path("/{username}/watchlist/{mediaId}")
    @PreAuthorize("@accessValidator.checkIsUserMe(#username)")
    @Produces(VndType.APPLICATION_WATCHLIST_MEDIA)
    public Response getWatchlistMediaByMediaId(@PathParam("username") final String username,
                                               @PathParam("mediaId") final int mediaId) {
        try {
            userService.isUsernameMe(username);
            boolean watchlist = mediaService.getMediaById(mediaId).isWatchlist();
            if(watchlist){
                return Response.ok(new MediaIdDto(mediaId, username, uriInfo)).build();
            }
            return Response.noContent().build();
        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }

    @DELETE
    @Path("/{username}/watchlist/{mediaId}")
    @PreAuthorize("@accessValidator.checkIsUserMe(#username)")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFromWatchlist(@PathParam("username") final String username,
                                        @PathParam("mediaId") final int mediaId){
        try {
            moovieListService.removeMediaFromWatchlist(mediaId, username);
            return Response.ok().build();
        } catch (UnableToFindUserException | NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /***
     * FOLLOWS
     */

    @GET
    @Path("/{username}/listFollows")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(VndType.APPLICATION_FOLLOWED_LISTS)
    public Response getFollowedLists(@PathParam("username") final String username,
                                     @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        try {
            int userid = userService.getProfileByUsername(username).getUserId();
            List<MoovieListCard> mlcList = moovieListService.getFollowedMoovieListCards(userid, MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                    PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), pageNumber - 1);

            int listCount = moovieListService.getFollowedMoovieListCardsCount(userid, MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType());

            List<UserListIdDto> listToRet = new ArrayList<UserListIdDto>();
            for ( MoovieListCard mlc : mlcList){
                listToRet.add(new UserListIdDto(mlc.getMoovieListId(), username));
            }

            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<UserListIdDto>>(listToRet) {
            });
            final PagingUtils<MoovieListCard> toReturnMoovieListCardList = new PagingUtils<>(mlcList, pageNumber, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), listCount);
            ResponseUtils.setPaginationLinks(res, toReturnMoovieListCardList, uriInfo);
            return res.build();
        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }


    @GET
    @Path("/{username}/listFollows/{listId}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(VndType.APPLICATION_FOLLOWED_LISTS_USER_LIST)
    public Response getUserFollowedListById(@PathParam("username") String username,
                                            @PathParam("listId") final int listId) {
        try {
            UserMoovieListId userMoovieListId = moovieListService.currentUserHasFollowed(listId);
            if (userMoovieListId != null && userMoovieListId.getUsername().equals(username)) {
                return Response.ok(UserListIdDto.fromUserMoovieList(userMoovieListId, username)).build();
            }
            return Response.noContent().build();
        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }

    /***
     * LIKES
     */

    // Returns a list of likes
    @GET
    @Path("/{username}/listLikes")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(VndType.APPLICATION_LIST_LIKE_LISTS)
    public Response getLikedLists(@PathParam("username") final String username,
                                  @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        try {
            int userid = userService.getProfileByUsername(username).getUserId();

            List<MoovieListCard> mlcList = moovieListService.getLikedMoovieListCards(username, MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                    PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), pageNumber - 1);



            int listCount = userService.getLikedMoovieListCountForUser(username);

            List<UserListIdDto> listToRet = new ArrayList<UserListIdDto>();
            for ( MoovieListCard mlc : mlcList){
                listToRet.add(new UserListIdDto(mlc.getMoovieListId(), username));
            }

            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<UserListIdDto>>(listToRet) {
            });
            final PagingUtils<MoovieListCard> toReturnMoovieListCardList = new PagingUtils<>(mlcList, pageNumber, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), listCount);
            ResponseUtils.setPaginationLinks(res, toReturnMoovieListCardList, uriInfo);
            return res.build();
        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }

    // Returns like status for a specific media
    @GET
    @Path("/{username}/listLikes/{listId}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(VndType.APPLICATION_LIST_LIKE)
    public Response getUserLikedListById(@PathParam("listId") final int listId,
                                         @PathParam("username") final String username) {
        try {
            UserMoovieListId userMoovieListId = moovieListService.currentUserHasLiked(listId);
            if (userMoovieListId != null && userMoovieListId.getUsername().equals(username)) {
                return Response.ok(UserListIdDto.fromUserMoovieList(userMoovieListId, username)).build();
            }
            return Response.noContent().build();
        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }


    @GET
    @Path("/{username}/reviewLikes/{reviewId}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(VndType.APPLICATION_REVIEW_LIKE)
    public Response getLikedReviewById(@PathParam("username") final String username,
                                       @PathParam("reviewId") final int reviewId) {
        try {
            Review review = reviewService.getReviewById(reviewId);
            boolean liked=review.isCurrentUserHasLiked();

            if(liked){
                return Response.ok(UserReviewIdDto.fromUserReviewId(reviewId,username)).build();
            }

            return Response.noContent().build();
        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }


    @GET
    @Path("/{username}/moovieListsReviewsLikes/{moovieListReviewId}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(VndType.APPLICATION_MOOVIELIST_REVIEW_LIKE)
    public Response getLikedMoovieListsReviewById(@PathParam("username") final String username,
                                       @PathParam("moovieListReviewId") final int moovieListReviewId) {
        try {
            MoovieListReview review = reviewService.getMoovieListReviewById(moovieListReviewId);
            boolean liked=review.isCurrentUserHasLiked();

            if(liked){
                return Response.ok(UserReviewIdDto.fromUserReviewId(moovieListReviewId,username)).build();
            }

            return Response.noContent().build();
        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }


    @GET
    @Path("/{username}/commentsFeedback/{commentId}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(VndType.APPLICATION_COMMENT_LIKE)
    public Response getFeedbackedCommentById(@PathParam("username") final String username,
                                                  @PathParam("commentId") final int commentId) {
        try {
            int uid = userService.getProfileByUsername(username).getUserId();
            boolean liked= commentService.userHasLiked(commentId, uid);
            boolean disliked=commentService.userHasDisliked(commentId, uid);

            if(liked || disliked){
                return Response.ok(UserCommentIdDto.fromUserCommentId(commentId,username,liked,disliked)).build();
            }
            return Response.noContent().build();

        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }
}
