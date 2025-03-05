package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.annotations.FieldsPasswordMustMatch;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FieldsPasswordMustMatch(message = "Passwords must match")
public class RegisterForm {
    @NotEmpty(message = "Please enter an username")
    @Size(min = 4, max = 20, message = "Must be between 4-20 characters")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Username should only contain alphanumerical characters")
    private String username;

    @Email(message = "Please enter a valid email")
    @NotEmpty(message = "Please enter an email")
    private String email;

    @NotEmpty(message = "Please enter a password")
    @Size(min = 6, max = 25, message = "Must be between 6-25 characters")
    private String password;

    @NotEmpty(message = "Please re-enter the password")
    @Size(min = 6, max = 100, message = "Must be between 6-25 characters")
    private String repeatPassword;

    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordMatch() {
        return password != null && password.equals(repeatPassword);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
