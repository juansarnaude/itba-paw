package ar.edu.itba.paw.models.MoovieList;

public class UserMoovieListId {
    private int mlId;
    private String username;

    public UserMoovieListId(int mlId, String username) {
        this.mlId = mlId;
        this.username = username;
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
