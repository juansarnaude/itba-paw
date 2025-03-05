package ar.edu.itba.paw.models.Reports;

import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.User.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportsreviews")
public class ReviewReport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportid")
    private int reportId;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "content", length = 5000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "reportedBy", referencedColumnName = "userId")
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "reviewId", referencedColumnName = "reviewId")
    private Review review;

    @Column(name = "report_date", nullable = false)
    private LocalDateTime report_date;

    @Column(name = "resolved", nullable = false)
    private Boolean resolved;
    
    ReviewReport(){}

    public ReviewReport(int type, String content, User reportedBy, Review review)
    {
        this.review = review;
        this.resolved = false;
        this.reportedBy = reportedBy;
        this.content = content;
        this.type = type;
        this.report_date = LocalDateTime.now();
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public void setReport_date(LocalDateTime report_date) {
        this.report_date = report_date;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public int getReportId() {
        return reportId;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public User getReportedBy() {
        return reportedBy;
    }

    public Review getReview() {
        return review;
    }

    public LocalDateTime getReport_date() {
        return report_date;
    }

    public Boolean getResolved() {
        return resolved;
    }
}
