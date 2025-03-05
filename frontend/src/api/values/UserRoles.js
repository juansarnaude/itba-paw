const UserRoles = {
    // MODERATOR_NOT_REGISTERED: -102,
    BANNED_NOT_REGISTERED: -101,
    BANNED: -2,
    // NOT_AUTHENTICATED: -1,
    // UNREGISTERED: 0,
    USER: 1,
    MODERATOR: 2,

    getRoleFromInt(role) {
        return Object.keys(this).find(key => this[key] === role) || null;
    }
};

export default UserRoles;