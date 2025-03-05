package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.MoovieList.UserMoovieListId;

public class UserListIdDto {
    private int mlId;
    private String username;

    public UserListIdDto() {}

    public UserListIdDto(int mlId, String username) {
        this.mlId = mlId;
        this.username = username;
    }

    public static UserListIdDto fromUserMoovieList(UserMoovieListId obj, String username){
        UserListIdDto dto = new UserListIdDto();
        dto.mlId = obj.getMlId();
        dto.username = username;
        return dto;
    }

    public int getMlId() {
        return mlId;
    }

    public void setMlId(int mlId) {
        this.mlId = mlId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

