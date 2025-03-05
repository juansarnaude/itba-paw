package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidAuthenticationLevelRequiredToPerformActionException;
import ar.edu.itba.paw.exceptions.UnableToBanUserException;
import ar.edu.itba.paw.exceptions.UnableToChangeRoleException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.Review.ReviewTypes;

public interface ModeratorService {
    //Deletes a Media Review
    void deleteReview(int reviewId, int mediaId, ReviewTypes type);

    //Delete a Moovie List
    void deleteMoovieListList(int moovieListId);

    //Bans an user
    void banUser(int userId, String message) throws UnableToBanUserException, InvalidAuthenticationLevelRequiredToPerformActionException;

    //Bans an user
    void unbanUser(int userId) throws UnableToFindUserException, UnableToChangeRoleException, InvalidAuthenticationLevelRequiredToPerformActionException;

    //Chane the tole of an user to Moderator
    void makeUserModerator(int userId);

    void deleteListReview(int moovieListReviewId);
}
