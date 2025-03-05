package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.User.Profile;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class ProfileDto {
    private int userId;
    private String username;
    private String email;
    private int role;
    private int moovieListCount;
    private int reviewsCount;
    private int milkyPoints;
    private boolean hasBadge;
    private String profilePictureUrl;
    private String url;

    public static ProfileDto fromProfile(final Profile profile, final UriInfo uriInfo) {
        final ProfileDto dto = new ProfileDto();

        dto.userId = profile.getUserId();
        dto.username = profile.getUsername();
        dto.email = profile.getEmail();
        dto.role = profile.getRole();
        dto.moovieListCount = profile.getMoovieListCount();
        dto.reviewsCount = profile.getReviewsCount();
        dto.milkyPoints = profile.getMilkyPoints();
        dto.hasBadge = profile.isHasBadge();

        if (profile.isHasPfp()) {
            dto.profilePictureUrl = uriInfo.getBaseUriBuilder().path("profiles/{username}/image").build(profile.getUsername()).toString();
        }
        dto.url = uriInfo.getBaseUriBuilder().path("profiles/{username}").build(profile.getUsername()).toString();
        return dto;
    }

    public static List<ProfileDto> fromProfileList(List<Profile> profileList, UriInfo uriInfo) {
        return profileList.stream().map(m -> fromProfile(m, uriInfo)).collect(java.util.stream.Collectors.toList());
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getMoovieListCount() {
        return moovieListCount;
    }

    public void setMoovieListCount(int moovieListCount) {
        this.moovieListCount = moovieListCount;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public int getMilkyPoints() {
        return milkyPoints;
    }

    public void setMilkyPoints(int milkyPoints) {
        this.milkyPoints = milkyPoints;
    }

    public boolean isHasBadge() {
        return hasBadge;
    }

    public void setHasBadge(boolean hasBadge) {
        this.hasBadge = hasBadge;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPictureUrl() {
        return profilePictureUrl;
    }

    public void setPictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

}
