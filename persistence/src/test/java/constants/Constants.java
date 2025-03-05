package constants;

import ar.edu.itba.paw.models.User.User;

public class Constants {

    //For Users Tests
    public static final int INSERTED_USER_ID = 2;
    public static final String INSERTED_USER_EMAIL = "cavani@test.com";
    public static final String INSERTED_USER_USERNAME = "Cavani";
    public static final String INSERTED_USER_PASSWORD = "password";
    public static final String TO_INSERT_USER_EMAIL = "moovie@test.com";
    public static final String TO_INSERT_USER_USERNAME = "testUser";
    public static final String TO_INSERT_USER_PASSWORD = "pass123";
    public static final String USERS_TABLE = "users";
    public static final int NON_EXISTENT_USER_ID = 5;

    // For Comments Tests
    public static final int TO_INSERT_COMMENT_REVIEW_ID = 2;
    public static final String TO_INSERT_COMMENT_DESCRIPTION = "My comment =)";
    public static final int TO_INSERT_COMMENT_USER_ID = 4;
    public static final String COMMENTS_TABLE = "comments";

    public static final int INSERTED_COMMENT_ID = 4;
    public static final int INSERTED_COMMENT_REVIEW_ID = 3;
    public static final int INSERTED_COMMENT_USER_ID = 2;
    public static final int PAGE_SIZE = 10;

    // For Moovie List Tests
    public static final int INSERTED_MOOVIELIST_ID = 2;

    public static final int TO_INSERT_MEDIALIST = 3;

    public static final String MOOVIELIST_TABLE = "moovielists";
    public static final String MOOVIELISTCONTENT_TABLE = "moovielistscontent";

    public static final String MOOVIELIST_NAME = "CavaniList";
    public static final String MOOVIELIST_DESCRIPTION = "CavaniListDescription";

    public static final int TO_INSERT_MOOVIE_LIST_ID = 4;
    public static final String TO_INSERT_MOOVIE_LIST_NAME = "New Moovie List";
    public static final String TO_INSERT_MOOVIE_LIST_DESCRIPTION = "New Moovie List Description";

    public static final int ID_TO_DELETE = 2;

    public static final int MEDIA_IN_MOOVIE_LIST = 9;
    public static final int MEDIA_NOT_IN_MOOVIE_LIST = 23;
    public static final int MOOVIELIST_FOLLOWERS = 1;
    public static final String NEW_MOOVIELIST_NAME = "New List Name";
    public static final String NEW_MOOVIELIST_DESCRIPTION = "New List Description";

    // For Review Tests
    public static final int INSERTED_REVIEW_ID = 2;
    public static final int INSERTED_REVIEW_USER_ID = 2;
    public static final int INSERTED_REVIEW_MEDIA_ID = 9;
    public static final int INSERTED_REVIEW_RATING = 5;
    public static final String INSERTED_REVIEW_DESCRIPTION = "Buena Peli";
    public static final int TO_INSERT_REVIEW_USER_ID = 2;
    public static final int TO_INSERT_REVIEW_MEDIA_ID = 4;
    public static final int TO_INSERT_REVIEW_RATING_ID = 4;
    public static final String TO_INSERT_REVIEW_DESCRIPTION = "My description";
    public static final String REVIEWS_TABLE = "reviews";

    //For Reports Tests
    public static final String REPORT_CONTENT = "This is a report";

    private Constants() {
        throw new AssertionError();
    }

    public static User getInsertedUser() {
        return new User.Builder(INSERTED_USER_USERNAME, INSERTED_USER_EMAIL, INSERTED_USER_PASSWORD, 0, 0).userId(INSERTED_USER_ID).build();
    }
}
