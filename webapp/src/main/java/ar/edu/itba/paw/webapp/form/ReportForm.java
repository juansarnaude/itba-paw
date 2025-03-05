package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;

public class ReportForm {


    @NotEmpty(message = "Report Type Required")
    @Range(min = 1, message = "Please enter a valid id")

    private int reportType;

    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Should only contain alphanumerical characters")
    private String type;

    private String content;

    @NotEmpty(message = "Report Type Required")
    @Range(min = 1, message = "Please enter a valid id")
    private int reportedBy;


    @NotEmpty(message = "Report Type Required")
    @Range(min = 1, message = "Please enter a valid id")
    private int id;

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public int getReportedBy() {
        return reportedBy;
    }

    public String getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReportedBy(int reportedBy) {
        this.reportedBy = reportedBy;
    }

    public void setType(String type) {
        this.type = type;
    }
}
