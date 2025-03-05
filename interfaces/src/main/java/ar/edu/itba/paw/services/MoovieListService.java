package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.OrderedMedia;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListDetails;
import ar.edu.itba.paw.models.MoovieList.UserMoovieListId;
import org.hibernate.criterion.Order;

import java.util.List;

public interface MoovieListService {


    //Get the moovieList object, doesnt contain  much info
    //For controllers is best to use the CARDS
    MoovieList getMoovieListById(int moovieListId);

    //Union of the content and the card of a moovieListId, also workds searching with name username
    MoovieListDetails getMoovieListDetails(int moovieListId, String name, String ownerUsername, String orderBy, String sortOrder, int size, int pageNumber);

    //Gets the moovieListCard (recomended for querys and contains useful info for the visualization of a MoovieList
    MoovieListCard getMoovieListCardById(int moovieListId);

    //Bro, just read the function name!
    List<MoovieListCard> getLikedMoovieListCards(String userId, int type, int size, int pageNumber);
    UserMoovieListId currentUserHasLiked( int moovieListId);

    //Returns the followed MoovieListsCards
    List<MoovieListCard> getFollowedMoovieListCards(int userId, int type, int size, int pageNumber);
    UserMoovieListId currentUserHasFollowed( int moovieListId);

    int getFollowedMoovieListCardsCount(int userId, int type);

    //Returns a list of recomended amount of moovie list, recomends are based on user who liked this list likes (if the return size < size its filled with random moovie lists)
    List<MoovieListCard> getRecommendedMoovieListCards(int moovieListId, int size, int pageNumber);

    //Returns a list of media of medias that could fit in the MoovieList provided.
    //Its calculated based on the best rated media from the most repeated genre in the list
    List<Media> getRecommendedMediaToAdd(int moovieListId, int size);


    //Get the MoovieListCard, which contains the element presented in searchs, has a lot of arguments for searchs/querys
    List<MoovieListCard> getMoovieListCards(String search, String ownerUsername , int type , String orderBy, String order, int size, int pageNumber);
    int getMoovieListCardsCount(String search, String ownerUsername , int type);


    //Get the content of media of some moovieList by its id
    //The isWatched is returned as false (in every element) if the user who makes the query is not the owner
    List<Media> getMoovieListContent(int moovieListId, String orderBy, String sortOrder, int size, int pageNumber);
    List<OrderedMedia> getMoovieListContentOrdered(int moovieListId, String orderBy, String sortOrder, int size, int pageNumber);

    //Featured List Functions
    List<Media> getFeaturedMoovieListContent( int mediaType, String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber);
    int countWatchedFeaturedMoovieListContent(int mediaType, String featuredListOrder);


    //Create or insert into moovieList
    MoovieList createMoovieList(String name, int type, String description);
    MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList);
    void deleteMediaFromMoovieList(int moovieListId, int mediaId);
    void deleteMoovieList(int moovieListId);
    void editMoovieList(int moovieListId, String name, String description);
    int isMediaInMoovieList(int mediaId, int moovieListId);

    // Receives one media id and the new customOrder
    void updateMoovieListOrder(int moovieListId, int mediaId, int newCustomOrder);

    //Likes functions
    boolean likeMoovieList(int moovieListId);
    boolean removeLikeMoovieList(int moovieListId);

    boolean followMoovieList(int moovieListId);
    boolean removeFollowMoovieList(int moovieListId);

    void addMediaToWatchlist(int movieId, String username);
    void removeMediaFromWatchlist(int movieId, String username);
    void addMediaToWatched(int movieId, String username);
    void removeMediaFromWatched(int movieId, String username);

}
