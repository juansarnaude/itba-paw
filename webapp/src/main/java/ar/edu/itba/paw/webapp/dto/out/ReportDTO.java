package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Reports.CommentReport;
import ar.edu.itba.paw.models.Reports.MoovieListReport;
import ar.edu.itba.paw.models.Reports.MoovieListReviewReport;
import ar.edu.itba.paw.models.Reports.ReviewReport;

import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;

public class ReportDTO {

    //    URL for the object that was reported
    //    /comment/{id}, /review/{id}, etc...
    private String url;
    private Boolean resolved;
    private LocalDateTime report_date;
    //    URL for the user that reported
    //    /user/{username}
    private String reportedByUrl;
    private int type;

    // No-argument constructor for JAXB
    public ReportDTO() {
    }

    public ReportDTO(String url, Boolean resolved, LocalDateTime report_date, String reportedByUrl, int type) {
        this.url = url;
        this.resolved = resolved;
        this.report_date = report_date;
        this.reportedByUrl = reportedByUrl;
        this.type = type;
    }

    public static ReportDTO fromReviewReport(ReviewReport reviewReport, UriInfo uriInfo) {
        String url = uriInfo.getBaseUriBuilder().path("/reviews/" + reviewReport.getReview().getReviewId()).toString();
        String reportedByUrl = uriInfo.getBaseUriBuilder().path("/users/" + reviewReport.getReportedBy().getUsername()).toString();
        return new ReportDTO(url, reviewReport.getResolved(), reviewReport.getReport_date(), reportedByUrl, reviewReport.getType());
    }

    public static ReportDTO fromCommentReport(CommentReport commentReport, UriInfo uriInfo) {
        String url = uriInfo.getBaseUriBuilder().path("/comments/" + commentReport.getComment().getCommentId()).toString();
        String reportedByUrl = uriInfo.getBaseUriBuilder().path("/users/" + commentReport.getReportedBy().getUsername()).toString();
        return new ReportDTO(url, commentReport.getResolved(), commentReport.getReport_date(), reportedByUrl, commentReport.getType());
    }

    public static ReportDTO fromMoovieListReviewReport(MoovieListReviewReport mlrReport, UriInfo uriInfo) {
        String url = uriInfo.getBaseUriBuilder().path("/moovieListReviews/" + mlrReport.getMoovieListReview().getMoovieListReviewId()).toString();
        String reportedByUrl = uriInfo.getBaseUriBuilder().path("/users/" + mlrReport.getReportedBy().getUsername()).toString();
        return new ReportDTO(url, mlrReport.getResolved(), mlrReport.getReport_date(), reportedByUrl, mlrReport.getType());
    }

    public static ReportDTO fromMoovieListReport(MoovieListReport mlReport, UriInfo uriInfo) {
        String url = uriInfo.getBaseUriBuilder().path("/lists/" + mlReport.getMoovieList().getMoovieListId()).toString();
        String reportedByUrl = uriInfo.getBaseUriBuilder().path("/users/" + mlReport.getReportedBy().getUsername()).toString();
        return new ReportDTO(url, mlReport.getResolved(), mlReport.getReport_date(), reportedByUrl, mlReport.getType());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public LocalDateTime getReport_date() {
        return report_date;
    }

    public void setReport_date(LocalDateTime report_date) {
        this.report_date = report_date;
    }

    public String getReportedByUrl() {
        return reportedByUrl;
    }

    public void setReportedByUrl(String reportedByUrl) {
        this.reportedByUrl = reportedByUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
