package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Cast.Director;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class DirectorDto {
    private int directorId;
    private String name;
    private long totalMedia;
    private String url;

    public static DirectorDto fromDirector(final Director director, final UriInfo uriInfo){
        final DirectorDto dto = new DirectorDto();
        dto.directorId = director.getDirectorId();
        dto.name = director.getName();
        dto.totalMedia = director.getTotalMedia();
        dto.url = uriInfo.getBaseUriBuilder().path("directors/{id}").build(director.getDirectorId()).toString();
        return dto;
    }

    public static List<DirectorDto> fromDirectorList(final List<Director> directorList, final UriInfo uriInfo) {
        return directorList.stream().map(m -> fromDirector(m, uriInfo)).collect(java.util.stream.Collectors.toList());
    }

    public int getDirectorId() {
        return directorId;
    }

    public void setDirectorId(int directorId) {
        this.directorId = directorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalMedia() {
        return totalMedia;
    }

    public void setTotalMedia(long totalMedia) {
        this.totalMedia = totalMedia;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
