package ar.edu.itba.paw.models.User;

public enum UserRoles {
    MODERATOR_NOT_REGISTERED(-102),
    BANNED_NOT_REGISTERED(-101),
    BANNED(-2),
    NOT_AUTHENTICATED(-1),
    UNREGISTERED(0),
    USER(1),
    MODERATOR(2);

    private final int role;
    UserRoles(int role){
        this.role = role;
    }

    public int getRole(){
        return role;
    }

    public static UserRoles getRoleFromInt(int role) {
        for (UserRoles userRole : UserRoles.values()) {
            if (userRole.getRole() == role) {
                return userRole;
            }
        }
        return null;
    }
}

