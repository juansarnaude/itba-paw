package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.ConflictException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.Reports.*;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.dto.in.ReportCreateDTO;
import ar.edu.itba.paw.webapp.dto.out.CountDto;
import ar.edu.itba.paw.webapp.dto.out.ReportDTO;
import ar.edu.itba.paw.webapp.mappers.UnableToFindUserEM;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("/reports")
@Component
public class ReportController {
    private final ReportService reportService;
    private final UserService userService;
    private final CommentService commentService;
    private final MoovieListService moovieListService;
    private final ReviewService reviewService;


    @Context
    private UriInfo uriInfo;

    @Autowired
    public ReportController(ReportService reportService, UserService userService, CommentService commentService, MoovieListService moovieListService, ReviewService reviewService) {
        this.reportService = reportService;
        this.userService = userService;
        this.commentService = commentService;
        this.moovieListService = moovieListService;
        this.reviewService = reviewService;
    }

    @GET
    @Produces(VndType.APPLICATION_REPORT_LIST)
    public Response getReports(@QueryParam("contentType") @NotNull String contentType) {
        if(contentType!=null && !contentType.equalsIgnoreCase("comment") && !contentType.equalsIgnoreCase("moovieList") && !contentType.equalsIgnoreCase("moovieListReview") && !contentType.equalsIgnoreCase("review")){
            throw new IllegalArgumentException("The 'contentType' query parameter must be one of 'comment', 'moovieList', 'moovieListReview', or 'review'.");
        }

        // Fetch reports based on filters using the ReportService
        List<Object> reports = reportService.getReports(contentType);

        // Map reports to DTOs
        List<ReportDTO> reportDTOs = reports.stream().map(report -> {
            if (report instanceof ReviewReport) {
                return ReportDTO.fromReviewReport((ReviewReport) report, uriInfo);
            } else if (report instanceof CommentReport) {
                return ReportDTO.fromCommentReport((CommentReport) report, uriInfo);
            } else if (report instanceof MoovieListReport) {
                return ReportDTO.fromMoovieListReport((MoovieListReport) report, uriInfo);
            } else if (report instanceof MoovieListReviewReport) {
                return ReportDTO.fromMoovieListReviewReport((MoovieListReviewReport) report, uriInfo);
            }
            return null;
        }).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<ReportDTO>>(reportDTOs) {
        }).build();
    }


    @POST
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_REPORT_FORM)
    @Produces(VndType.APPLICATION_REPORT)
    public Response report(
            @QueryParam("commentId") final Integer commentId,
            @QueryParam("moovieListId") final Integer moovieListId,
            @QueryParam("moovieListReviewId") final Integer moovieListReviewId,
            @QueryParam("reviewId") final Integer reviewId,
            @Valid final ReportCreateDTO reportDTO) {
        try {
            User currentUser = userService.getInfoOfMyUser();

            if (commentId != null) {
                // Lógica para reportar un comentario
                CommentReport response = reportService.reportComment(
                        commentId,
                        currentUser.getUserId(),
                        reportDTO.getType()

                );
                return Response.ok(ReportDTO.fromCommentReport(response, uriInfo)).build();
            } else if (moovieListId != null) {
                // Lógica para reportar una lista de películas
                MoovieListReport response = reportService.reportMoovieList(
                        moovieListId,
                        currentUser.getUserId(),
                        reportDTO.getType()

                );
                return Response.ok(ReportDTO.fromMoovieListReport(response, uriInfo)).build();
            } else if (moovieListReviewId != null) {
                // Lógica para reportar una reseña de lista de películas
                MoovieListReviewReport response = reportService.reportMoovieListReview(
                        moovieListReviewId,
                        currentUser.getUserId(),
                        reportDTO.getType()
                );
                return Response.ok(ReportDTO.fromMoovieListReviewReport(response, uriInfo)).build();
            } else if (reviewId != null) {
                // Lógica para reportar una reseña general
                ReviewReport response = reportService.reportReview(
                        reviewId,
                        currentUser.getUserId(),
                        reportDTO.getType()
                );
                return Response.ok(ReportDTO.fromReviewReport(response, uriInfo)).build();
            } else {
                throw new IllegalArgumentException("At least one of 'commentId', 'reviewId', 'moovieListReviewId', or 'generalReviewId' must be provided.");
            }
        }
        catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @DELETE
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resolveReport(
            @QueryParam("moovieListId") final Integer moovieListId,
            @QueryParam("commentId") final Integer commentId,
            @QueryParam("moovieListReviewId") final Integer moovieListReviewId,
            @QueryParam("reviewId") final Integer reviewId) {
        try {
            if (moovieListId != null) {
                // Resolver reporte de una lista de películas
                reportService.resolveMoovieListReport(moovieListId);
            } else if (commentId != null) {
                // Resolver reporte de un comentario
                reportService.resolveCommentReport(commentId);
            } else if (moovieListReviewId != null) {
                // Resolver reporte de una reseña de lista de películas
                reportService.resolveMoovieListReviewReport(moovieListReviewId);
            } else if (reviewId != null) {
                // Resolver reporte de una reseña general
                reportService.resolveReviewReport(reviewId);
            } else {
                throw new IllegalArgumentException("At least one of 'moovieListId', 'commentId', 'moovieListReviewId', or 'reviewId' must be provided.");
            }
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage(), e);
        }
        catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @GET
    @Path("/count")
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Produces(VndType.APPLICATION_REPORT_COUNT)
    public Response getReportCount(@QueryParam("contentType") String contentType,@QueryParam("reportType") Integer reportType, @QueryParam("resourceId") Integer resourceId) {
        try {
            int totalReports;
            if (contentType == null) {
                totalReports = reportService.getTotalReports();
                return Response.ok(CountDto.fromCount(totalReports)).build();
            } else if (resourceId==null && reportType!=null) {
                throw new IllegalArgumentException("The 'resourceId' query parameter must be provided when 'reportType' is provided.");
            } else if (contentType.equals("comment")) {
                if (resourceId == null && reportType == null) {
                    totalReports = reportService.getReportedCommentsCount();
                }
                else if ( reportType == null) {
                    totalReports = commentService.getCommentById(resourceId).getTotalReports();
                } else {
                    if (reportType == ReportTypesEnum.hatefulContent.getType())
                        totalReports =commentService.getCommentById(resourceId).getHateReports();
                    else if (reportType == ReportTypesEnum.abuse.getType())
                        totalReports = commentService.getCommentById(resourceId).getAbuseReports();
                    else if (reportType == ReportTypesEnum.spam.getType())
                        totalReports = commentService.getCommentById(resourceId).getSpamReports();
                    else if (reportType == ReportTypesEnum.privacy.getType())
                        totalReports = commentService.getCommentById(resourceId).getPrivacyReports();
                    else {
                        throw new IllegalArgumentException("The 'reportType' query parameter must be one of 'hatefulContent', 'abuse', 'spam', or 'privacy'.");
                    }
                }
                return Response.ok(CountDto.fromCount(totalReports)).build();
            } else if (contentType.equals("moovieList")) {
                if (resourceId == null && reportType == null) {
                    totalReports = reportService.getReportedMoovieListsCount();
                }
                else if ( reportType == null) {
                    totalReports = moovieListService.getMoovieListById(resourceId).getTotalReports();
                } else {
                    if (reportType == ReportTypesEnum.hatefulContent.getType())
                        totalReports =moovieListService.getMoovieListById(resourceId).getHateReports();
                    else if (reportType == ReportTypesEnum.abuse.getType())
                        totalReports = moovieListService.getMoovieListById(resourceId).getAbuseReports();
                    else if (reportType == ReportTypesEnum.spam.getType())
                        totalReports = moovieListService.getMoovieListById(resourceId).getSpamReports();
                    else if (reportType == ReportTypesEnum.privacy.getType())
                        totalReports = moovieListService.getMoovieListById(resourceId).getPrivacyReports();
                    else {
                        throw new IllegalArgumentException("The 'reportType' query parameter must be one of 'hatefulContent', 'abuse', 'spam', or 'privacy'.");
                    }
                }
                return Response.ok(CountDto.fromCount(totalReports)).build();
            } else if (contentType.equals("moovieListReview")) {
                if (resourceId == null && reportType == null) {
                    totalReports = reportService.getReportedMoovieListReviewsCount();
                }
                else if ( reportType == null) {
                    totalReports = reviewService.getMoovieListReviewById(resourceId).getTotalReports();
                }
                else {
                    if (reportType == ReportTypesEnum.hatefulContent.getType())
                        totalReports =reviewService.getMoovieListReviewById(resourceId).getHateReports();
                    else if (reportType == ReportTypesEnum.abuse.getType())
                        totalReports = reviewService.getMoovieListReviewById(resourceId).getAbuseReports();
                    else if (reportType == ReportTypesEnum.spam.getType())
                        totalReports = reviewService.getMoovieListReviewById(resourceId).getSpamReports();
                    else if (reportType == ReportTypesEnum.privacy.getType())
                        totalReports =reviewService.getMoovieListReviewById(resourceId).getPrivacyReports();
                    else {
                        throw new IllegalArgumentException("The 'reportType' query parameter must be one of 'hatefulContent', 'abuse', 'spam', or 'privacy'.");
                    }
                }
                return Response.ok(CountDto.fromCount(totalReports)).build();
            } else if (contentType.equals("review")) {
                if (resourceId == null && reportType == null) {
                        totalReports = reportService.getReportedReviewsCount();
                }
                else if ( reportType == null) {
                    totalReports = reviewService.getReviewById(resourceId).getTotalReports();
                } else {
                    if (reportType == ReportTypesEnum.hatefulContent.getType())
                        totalReports =reviewService.getReviewById(resourceId).getHateReports();
                    else if (reportType == ReportTypesEnum.abuse.getType())
                        totalReports = reviewService.getReviewById(resourceId).getAbuseReports();
                    else if (reportType == ReportTypesEnum.spam.getType())
                        totalReports = reviewService.getReviewById(resourceId).getSpamReports();
                    else if (reportType == ReportTypesEnum.privacy.getType())
                        totalReports = reviewService.getReviewById(resourceId).getPrivacyReports();
                    else {
                        throw new IllegalArgumentException("The 'reportType' query parameter must be one of 'hatefulContent', 'abuse', 'spam', or 'privacy'.");
                    }
                }
                return Response.ok(CountDto.fromCount(totalReports)).build();
            } else {
                throw new IllegalArgumentException("The 'contentType' query parameter must be one of 'comment', 'moovieList', 'moovieListReview', or 'review'.");
            }

        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage(), e);
        }
        catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

}
