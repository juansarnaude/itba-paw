package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.User.User;

import java.util.List;
import java.util.Optional;


public interface MoovieListDao {



    Optional<MoovieList> getMoovieListById(int moovieListId);

    MoovieListCard getMoovieListCardById(int moovieListId, int currentUserId);
    List<MoovieListCard> getLikedMoovieListCards(int userId,int type, int size, int pageNumber, int currentUserId);
    List<MoovieListCard> getFollowedMoovieListCards(int userId, int type, int size, int pageNumber, int currentUserId);
    List<User> getMoovieListFollowers(int moovieListId);
    int getFollowedMoovieListCardsCount(int userId, int type);

    List<MoovieListCard> getRecommendedMoovieListCards(int moovieListId, int size, int pageNumber, int currentUserId);
    List<Media> getRecommendedMediaToAdd(int moovieListId, int size);

    List<MoovieListCard> getMoovieListCards(String search, String ownerUsername , int type , String orderBy, String order, int size, int pageNumber, int currentUserId);
    int getMoovieListCardsCount(String search, String ownerUsername , int type);

    List<Media> getMoovieListContent(int moovieListId, int userid, String orderBy, String sortOrder, int size, int pageNumber);


    List<MoovieListContent> getMoovieListContentModel(int moovieListId, int size, int pageNumber);
    void updateMoovieListOrder(int moovieListId, int mediaId, int prevOrder, int newOrder);


    List<Media> getFeaturedMoovieListContent( int mediaType, int userid, String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber);
    int countWatchedFeaturedMoovieListContent(int mediaType, int userid, String featuredListOrder);


    MoovieList createMoovieList(int userId, String name, int type, String description);
    MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList);
    void deleteMediaFromMoovieList(int moovieListId, int mediaId);
    void deleteMoovieList(int moovieListId);
    void editMoovieList(int moovieListId, String name, String description);
    int isMediaInMoovieList(int mediaId, int moovieListId);


    void removeLikeMoovieList(int userId, int moovieListId);
    void likeMoovieList(int userId, int moovieListId);

    void removeFollowMoovieList(int userId, int moovieListId);
    void followMoovieList(int userId, int moovieListId);

    void deleteListReview(int moovieListReviewId);

    }
