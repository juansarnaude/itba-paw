package ar.edu.itba.paw.webapp.dto.in;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class FollowMoovieListDto {

    @Pattern(regexp ="^(FOLLOW|UNFOLLOW)$")
    @NotNull
    private String actionType;


    @NotNull
    private String username;

    public FollowMoovieListDto() {
    }

    public FollowMoovieListDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
