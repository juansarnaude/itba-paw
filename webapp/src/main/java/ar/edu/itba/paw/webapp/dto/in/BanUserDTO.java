package ar.edu.itba.paw.webapp.dto.in;

import ar.edu.itba.paw.models.User.UserRoles;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class BanUserDTO {

    @Pattern(regexp ="^(BAN|UNBAN)$")
    @NotNull
    private String modAction;

    private String banMessage;


    public UserRoles transformToEnum() {
        if (modAction.equals("BAN")) {
            return UserRoles.BANNED;
        } else if (modAction.equals("UNBAN")) {
            return UserRoles.USER;
        }
        return null;
    }


    public String getModAction() {
        return modAction;
    }

    public void setModAction(String modAction) {
        this.modAction = modAction;
    }

    public String getBanMessage() {
        return banMessage;
    }

    public void setBanMessage(String banMessage) {
        this.banMessage = banMessage;
    }
}
