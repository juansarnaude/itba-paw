package ar.edu.itba.paw.webapp.dto.in;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditListDTO {

    @NotEmpty(message = "Please enter a list name")
    @Pattern(regexp = "^(?!\\s+$)(?!.*[\\n\\r]).*$", message = "List name must not contain enters or only spaces")
    @Size(max = 50, message = "List name must be less than 50 characters")
    private String listName;

    @Pattern(regexp = "^(?!\\s+$)(?!.*[\\n\\r]).*$", message = "Description content must not contain enters or only spaces")
    @Size(max = 255, message = "List description must be less than 255 characters")
    private String listDescription;


    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setListDescription(String listDescription) {
        this.listDescription = listDescription;
    }

    public String getListName() {
        return listName;
    }

    public String getListDescription() {
        return listDescription;
    }

}
