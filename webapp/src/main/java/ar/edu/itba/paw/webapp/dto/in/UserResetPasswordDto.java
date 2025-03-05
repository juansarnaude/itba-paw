package ar.edu.itba.paw.webapp.dto.in;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserResetPasswordDto {

    @NotNull
    @Size(min = 8, max = 100)
    private String password;

    @NotNull
    private String token;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
