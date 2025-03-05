package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.UserMoovieListId;

import javax.ws.rs.core.UriInfo;

public class MediaIdDto {
    private int mediaId;
    private String mediaUrl;
    private String username;
    private String userUrl;

    public MediaIdDto() {}

    public MediaIdDto(int mediaId, String username, UriInfo uriInfo) {
        this.mediaId = mediaId;
        this.username = username;
        this.mediaUrl = uriInfo.getBaseUriBuilder().path("medias/{mediaId}").build(mediaId).toString();
        this.userUrl = uriInfo.getBaseUriBuilder().path("username/{username}").build(username).toString();
    }

    public MediaIdDto fromUserMedia(Media obj, String username, UriInfo uriInfo){
        MediaIdDto dto = new MediaIdDto();
        dto.mediaId = obj.getMediaId();
        dto.username = username;
        dto.mediaUrl = uriInfo.getBaseUriBuilder().path("medias/{mediaId}").build(mediaId).toString();
        dto.userUrl = uriInfo.getBaseUriBuilder().path("username/{username}").build(username).toString();
        return dto;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }
}

