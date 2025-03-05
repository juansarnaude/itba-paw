package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.ForbiddenException;
import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.OrderedMedia;
import ar.edu.itba.paw.models.MoovieList.*;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.persistence.MoovieListDao;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MoovieListServiceImpl implements MoovieListService{
    @Autowired
    private MoovieListDao moovieListDao;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MediaService mediaService;

    private static final int EVERY_THIS_AMOUNT_OF_LIKES_SEND_EMAIL = 5;

    private static final int EVERY_THIS_AMOUNT_OF_FOLLOWS_SEND_EMAIL = 5;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Transactional(readOnly = true)
    @Override
    public MoovieList getMoovieListById(int moovieListId) { //Check permissions
        MoovieList ml = moovieListDao.getMoovieListById(moovieListId).orElseThrow( () -> new MoovieListNotFoundException("Moovie list by id: " + moovieListId + " not found"));
        if( ml.getType() == MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType() || ml.getType() == MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType() ){
            try{
                if(ml.getUserId() != userService.tryToGetCurrentUserId()){
                    throw new ForbiddenException("User is not owner of the list and its private");
                }
            } catch (UserNotLoggedException e){
                throw new InvalidAccessToResourceException("User is not owner of the list and its private");
            }
        }
        return ml;
    }

    @Transactional(readOnly = true)
    @Override
    public MoovieListCard getMoovieListCardById(int moovieListId) {
        int currentUserId = userService.tryToGetCurrentUserId();
        MoovieListCard mlc = moovieListDao.getMoovieListCardById(moovieListId, currentUserId);
        if( mlc.getType() == MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType() || mlc.getType() == MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType()){
            try {
                if( mlc.getUserId() != currentUserId){
                    throw new InvalidAccessToResourceException("User is not owner of the list and its private");
                }
            }catch (UserNotLoggedException e ){
                throw new InvalidAccessToResourceException("User is not owner of the list and its private");
            }
        }
        return mlc;
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderedMedia> getMoovieListContentOrdered(int moovieListId, String orderBy, String sortOrder, int size, int pageNumber) {
        MoovieList ml = getMoovieListById(moovieListId);
        //If the previous didnt throw exception, we have the permissions needed to perform the next action
        int userid = userService.tryToGetCurrentUserId();
        List<Media> medias = moovieListDao.getMoovieListContent(moovieListId, userid , setOrderMediaBy(orderBy) , setSortOrder(sortOrder) ,size, pageNumber-1);
        List<OrderedMedia> toRet = new ArrayList<>();
        for(Media media : medias){
            toRet.add(new OrderedMedia(media, isMediaInMoovieList(media.getMediaId(), moovieListId)));
        }
        return toRet;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Media> getMoovieListContent(int moovieListId, String orderBy, String sortOrder, int size, int pageNumber) {
        MoovieList ml = getMoovieListById(moovieListId);
        //If the previous didnt throw exception, we have the permissions needed to perform the next action
        int userid = userService.tryToGetCurrentUserId();
        return moovieListDao.getMoovieListContent(moovieListId, userid, setOrderMediaBy(orderBy), setSortOrder(sortOrder), size, pageNumber - 1);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Media> getFeaturedMoovieListContent( int mediaType, String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber) {
        int userId = userService.tryToGetCurrentUserId();
        return moovieListDao.getFeaturedMoovieListContent(mediaType, userId, featuredListOrder, setOrderMediaBy(orderBy) , setSortOrder(sortOrder) ,size, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public int countWatchedFeaturedMoovieListContent(int mediaType, String featuredListOrder){
        return moovieListDao.countWatchedFeaturedMoovieListContent(mediaType, userService.tryToGetCurrentUserId(), featuredListOrder);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListCard> getMoovieListCards(String search, String ownerUsername , int type , String orderBy, String order, int size, int pageNumber) {
        if(type == MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType() || type == MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType()){
            if(!userService.getInfoOfMyUser().getUsername().equals(ownerUsername)){
                throw new InvalidAccessToResourceException("Need to be owner to acces the private list of this user");
            }
        }
        return moovieListDao.getMoovieListCards(search, ownerUsername, type,setOrderListsBy(orderBy) , setSortOrder(order), size, pageNumber-1, userService.tryToGetCurrentUserId());
    }

    @Transactional(readOnly = true)
    @Override
    public int getMoovieListCardsCount(String search, String ownerUsername , int type){
        return moovieListDao.getMoovieListCardsCount(search,ownerUsername,type);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListCard> getLikedMoovieListCards(String username,int type, int size, int pageNumber){
        return moovieListDao.getLikedMoovieListCards(userService.getProfileByUsername(username).getUserId(), type, size, pageNumber, userService.tryToGetCurrentUserId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListCard> getFollowedMoovieListCards(int userId, int type, int size, int pageNumber){
        return moovieListDao.getFollowedMoovieListCards(userId, type, size, pageNumber, userService.tryToGetCurrentUserId());
    }

    @Transactional(readOnly = true)
    @Override
    public int getFollowedMoovieListCardsCount(int userId, int type) {
        return moovieListDao.getFollowedMoovieListCardsCount(userId,type);
    }


    @Transactional(readOnly = true)
    @Override
    public List<MoovieListCard> getRecommendedMoovieListCards(int moovieListId, int size, int pageNumber){
        getMoovieListById(moovieListId);
        List<MoovieListCard> mlc =  moovieListDao.getRecommendedMoovieListCards(moovieListId, size, pageNumber, userService.tryToGetCurrentUserId());
        if(mlc.size()<size){
            // 5 are searched in order to be 100% sure there wont be repeating elements
            List<MoovieListCard> aux =  moovieListDao.getMoovieListCards(null, null, MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(), "random()","asc", size + 1, 0, userService.tryToGetCurrentUserId());
            // A check is needed so as no to add duplicates
            boolean flag;
            for(MoovieListCard mlcAux : aux ){
                flag = true;
                for(MoovieListCard mlcOriginal : mlc){
                    if(mlcOriginal.getMoovieListId() == mlcAux.getMoovieListId() || moovieListId == mlcAux.getMoovieListId()){
                        flag = false;
                    }
                }
                if(flag){
                    mlc.add(mlcAux);
                    if(mlc.size()==size){
                        return mlc;
                    }
                }
            }
        }
        return mlc;
    }



    @Transactional(readOnly = true)
    @Override
    public List<Media> getRecommendedMediaToAdd(int moovieListId, int size) {
        return moovieListDao.getRecommendedMediaToAdd(moovieListId, size);
    }

    @Transactional(readOnly = true)
    @Override
    public MoovieListDetails getMoovieListDetails(int moovieListId, String name, String ownerUsername, String orderBy, String sortOrder, int size, int pageNumber) {
        MoovieListCard card = null;
        List<Media> content = null;

        int currentUserId = userService.tryToGetCurrentUserId();

        if(moovieListId == -1){
            List<MoovieListCard> cards = moovieListDao.getMoovieListCards(name,ownerUsername, MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(), null, null, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), 0 , currentUserId);
            if(cards.size() != 1){
                throw new MoovieListNotFoundException("MoovieList: " + name+ " of: " +ownerUsername+ " not found");
            }
             card = cards.get(0);
             content = getMoovieListContent(card.getMoovieListId(),  setOrderMediaBy(orderBy) , setSortOrder(sortOrder),size,pageNumber);
        }
        else{
            card = moovieListDao.getMoovieListCardById(moovieListId, currentUserId);
            content = getMoovieListContent(moovieListId,setOrderMediaBy(orderBy) , setSortOrder(sortOrder),size,pageNumber);
        }
        return new MoovieListDetails(card,content);

    }

    @Transactional
    @Override
    public MoovieList createMoovieList(String name, int type, String description){
       return moovieListDao.createMoovieList(userService.getInfoOfMyUser().getUserId(), name, type, description);
    }

    @Transactional
    @Override
    public void editMoovieList(int moovieListId, String name, String description) {
        MoovieList moovieList = getMoovieListById(moovieListId);
        int currentUserId = userService.getInfoOfMyUser().getUserId();
        if(moovieList.getUserId() != currentUserId){
            throw new ForbiddenException("User must be owner to edit the list");
        }
        if(moovieList.getName().equals("Watched") || moovieList.getName().equals("Watchlist")){
            throw new IllegalArgumentException("This list type cant be edited");
        }
        moovieListDao.editMoovieList(moovieListId, name, description);
    }

    @Transactional
    @Override
    public MoovieList insertMediaIntoMoovieList(int moovieListId, List<Integer> mediaIdList) {
        MoovieList ml = getMoovieListById(moovieListId);
        int currentUserId = userService.tryToGetCurrentUserId();
        if(ml.getUserId() == currentUserId){
            // Validate the medias
            for(int mId : mediaIdList){
                mediaService.getMediaById(mId);

            }
            List<User> followers = moovieListDao.getMoovieListFollowers(moovieListId);
            LOGGER.info("About to insert media into empty list {}", moovieListId);
            MoovieList mlRet = moovieListDao.insertMediaIntoMoovieList(moovieListId, mediaIdList);
            followers.forEach( user -> {
                emailService.sendMediaAddedToFollowedListMail(user,ml, LocaleContextHolder.getLocale());
            });
            LOGGER.info("Succesfully inserted media: {} in list: {}.", mediaIdList,moovieListId);
            return mlRet;
        }
        else{
            throw new InvalidAccessToResourceException("User is not owner of the list");
        }
    }

    @Transactional
    @Override
    public int isMediaInMoovieList(int mediaId, int moovieListId) {
        getMoovieListById(moovieListId); // for the function to abort if it should be innacessible
        return moovieListDao.isMediaInMoovieList(mediaId,moovieListId);
    }

    @Transactional
    @Override
    public void deleteMediaFromMoovieList(int moovieListId, int mediaId) {
        MoovieList ml = getMoovieListById(moovieListId);
        if(ml.getUserId() == userService.tryToGetCurrentUserId()){
            moovieListDao.deleteMediaFromMoovieList(moovieListId, mediaId);
            LOGGER.info("Succesfully deleted media: {} from list: {}.", mediaId,moovieListId);
        }
        else{
            throw new InvalidAccessToResourceException("User is not owner of the list");
        }
    }

    @Transactional
    @Override
    public void deleteMoovieList(int moovieListId) {
        MoovieList ml = getMoovieListById(moovieListId);
        int currentUserId = userService.tryToGetCurrentUserId();
        if(userService.getInfoOfMyUser().getRole() == UserRoles.MODERATOR.getRole() || currentUserId == ml.getUserId()){
            moovieListDao.deleteMoovieList(moovieListId);
            LOGGER.info("Succesfully deleted list: {}.",moovieListId);

        }else{
            throw new InvalidAccessToResourceException("You are not the user of this list, so you can't delete it");
        }
    }

    @Transactional
    @Override
    public void updateMoovieListOrder(int moovieListId, int mediaId, int newCustomOrder) {
        MoovieListCard ml = getMoovieListCardById(moovieListId);
        if (ml.getUserId() != userService.tryToGetCurrentUserId()){
            throw new ForbiddenException("User must be owner to edit the list");
        }
        if(ml.getSize() < newCustomOrder || newCustomOrder < 1){
            throw new InvalidAccessToResourceException("New custom order must be lower than the size of the list and bigger than 1.");
        }
        int currentOrder = isMediaInMoovieList(mediaId, moovieListId);
        if(currentOrder == newCustomOrder){
            return;
        }
        moovieListDao.updateMoovieListOrder(moovieListId, mediaId, currentOrder, newCustomOrder);
    }




    @Transactional
    @Override
    public boolean likeMoovieList(int moovieListId) {
        int userId = userService.tryToGetCurrentUserId();
        if(userId==-1){
            throw new UserNotLoggedException();
        }
        MoovieListCard mlc = getMoovieListCardById(moovieListId);
        if (mlc.getType()==MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType() || mlc.getType()==MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType()){
            throw new ForbiddenException("User can't like a private list");
        }
        LOGGER.info("userID: {} -- will like {}  -- likestate is: {}",userId,mlc.getName(), mlc.isCurrentUserHasLiked());
        if(mlc.getType() == MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType()){
            if(mlc.isCurrentUserHasLiked()){
                return false;
            } else {
                moovieListDao.likeMoovieList(userId, moovieListId);
                int likeCountForMoovieList = mlc.getLikeCount();
                if(likeCountForMoovieList != 0 && (likeCountForMoovieList % EVERY_THIS_AMOUNT_OF_LIKES_SEND_EMAIL) == 0){
                    MoovieList mvlAux = getMoovieListById(moovieListId);
                    User toUser = userService.findUserById(mvlAux.getUserId());
                    emailService.sendNotificationLikeMilestoneMoovieListMail(toUser,likeCountForMoovieList,mvlAux,LocaleContextHolder.getLocale());
                    LOGGER.info("notificationLikeMilestoneMoovieList mail was sent to user : {} for the list: {}.", toUser.getUsername(), moovieListId);
                }
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean removeLikeMoovieList(int moovieListId) {
        int userId = userService.tryToGetCurrentUserId();
        if(userId==-1){
            throw new UserNotLoggedException();
        }
        MoovieListCard mlc = getMoovieListCardById(moovieListId);
        if(mlc.isCurrentUserHasLiked()){
            moovieListDao.removeLikeMoovieList(userId, moovieListId);
            LOGGER.info("Succesfully unliked list: {}, user: {}.",moovieListId,userId);
            return true;
        }
        return false;
    }



    @Transactional
    @Override
    public UserMoovieListId currentUserHasLiked( int moovieListId){
        String currentUsername = userService.getInfoOfMyUser().getUsername();
        MoovieListCard mlc = getMoovieListCardById(moovieListId);

        if(mlc.isCurrentUserHasLiked()){
            return new UserMoovieListId(mlc.getMoovieListId(), currentUsername);
        }
        return null;
    }




    @Transactional
    @Override
    public boolean followMoovieList(int moovieListId) {
        int userId = userService.tryToGetCurrentUserId();
        MoovieListCard mlc = getMoovieListCardById(moovieListId);
        if (mlc.getType()==MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType() || mlc.getType()==MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType()){
            throw new ForbiddenException("User can't follow a private list");
        }
        if(mlc.getType() == MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType()) {
            if (mlc.isCurrentUserHasFollowed()) {
                return false;
            } else {
                moovieListDao.followMoovieList(userId, moovieListId);
                int followCountForMoovieList = mlc.getFollowerCount();
                if (followCountForMoovieList != 0 && (followCountForMoovieList % EVERY_THIS_AMOUNT_OF_FOLLOWS_SEND_EMAIL) == 0) {
                    MoovieList mvlAux = getMoovieListById(moovieListId);
                    User toUser = userService.findUserById(mvlAux.getUserId());
                    emailService.sendNotificationFollowMilestoneMoovieListMail(toUser,followCountForMoovieList,mvlAux,LocaleContextHolder.getLocale());
                    LOGGER.info("notificationFollowMilestoneMoovieList mail was sent to user : {} for the list: {}.", toUser.getUsername(), moovieListId);
                }
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public UserMoovieListId currentUserHasFollowed( int moovieListId){
        String currentUsername = userService.getInfoOfMyUser().getUsername();
        MoovieListCard mlc = getMoovieListCardById(moovieListId);

        if(mlc.isCurrentUserHasFollowed()){
            return new UserMoovieListId(mlc.getMoovieListId(), currentUsername);
        }
        return null;
    }

    @Transactional
    @Override
    public boolean removeFollowMoovieList(int moovieListId) {
        int userId = userService.tryToGetCurrentUserId();
        MoovieListCard mlc = getMoovieListCardById(moovieListId);

        if(mlc.isCurrentUserHasFollowed()){
            moovieListDao.removeFollowMoovieList(userId, moovieListId);
            LOGGER.info("Succesfully unfollowed list: {}, user: {}.",moovieListId,userId);
            return true;
        }
        return false;
    }

    private String setSortOrder(String sortOrder){
        if(sortOrder==null || sortOrder.isEmpty()){
            return null;
        }
        sortOrder = sortOrder.replaceAll(" ","");
        if(sortOrder.toLowerCase().equals("asc")){
            return "asc";
        }
        if(sortOrder.toLowerCase().equals("desc")){
            return "desc";
        }
        return null;
    }



    @Transactional
    @Override
    public void addMediaToWatchlist(int mediaId, String username){
        userService.isUsernameMe(username);
        int mlId = getMoovieListCards("Watchlist",username,
                MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,1).get(0).getMoovieListId();
        insertMediaIntoMoovieList(mlId, Collections.singletonList(mediaId));
    }



    @Transactional
    @Override
    public void removeMediaFromWatchlist(int movieId, String username){
        userService.isUsernameMe(username);
        int mlId = getMoovieListCards("Watchlist",username,
                MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,1).get(0).getMoovieListId();
        deleteMediaFromMoovieList(mlId, movieId);
    }


    @Transactional
    @Override
    public void addMediaToWatched(int mediaId, String username){
        userService.isUsernameMe(username);
        int mlId = getMoovieListCards("Watched",username,
                MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,1).get(0).getMoovieListId();
        insertMediaIntoMoovieList(mlId, Collections.singletonList(mediaId));
    }

    @Transactional
    @Override
    public void removeMediaFromWatched(int movieId, String username){
        userService.isUsernameMe(username);
        int mlId = getMoovieListCards("Watched",username,
                MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,1).get(0).getMoovieListId();
        deleteMediaFromMoovieList(mlId, movieId);
    }



    private String setOrderMediaBy(String orderBy){
        if(orderBy==null || orderBy.isEmpty()){
            return null;
        }
        orderBy = orderBy.replaceAll(" ","");
        if(orderBy.toLowerCase().equals("tmdbrating")){
            return "tmdbRating";
        }
        if(orderBy.toLowerCase().equals("name")){
            return "name";
        }
        if(orderBy.toLowerCase().equals("releasedate")){
            return "releaseDate";
        }
        if(orderBy.toLowerCase().equals("totalrating")){
            return "totalRating";
        }
        if(orderBy.toLowerCase().equals("votecount")){
            return "voteCount";
        }
        if(orderBy.toLowerCase().equals("customorder")){
            return "customOrder";
        }
        if(orderBy.toLowerCase().equals("type")){
            return "type";
        }
        return null;
    }

    private String setOrderListsBy(String orderBy){
        if(orderBy==null || orderBy.isEmpty()){
            return null;
        }
        orderBy = orderBy.replaceAll(" ","");
        if(orderBy.toLowerCase().equals("type")){
            return "type";
        }
        if(orderBy.toLowerCase().equals("username")){
            return "username";
        }
        if(orderBy.toLowerCase().equals("likecount")){
            return "likeCount";
        }
        if(orderBy.toLowerCase().equals("followercount")){
            return "followerCount";
        }
        if(orderBy.toLowerCase().equals("moovielistid")){
            return "moovieListId";
        }
        return null;
    }
}
