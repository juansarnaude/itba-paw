package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.MediaNotFoundException;
import ar.edu.itba.paw.models.Cast.Director;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.persistence.MediaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class MediaServiceImpl implements MediaService{
    @Autowired
    private MediaDao mediaDao;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    @Override
    public List<Media> getMedia(int type, String search, String participant, List<Integer> genres, List<Integer> providers, List<String> status, List<String> lang, String orderBy, String sortOrder, int size, int pageNumber){
        return mediaDao.getMedia(type, search, participant,  genres, providers, status, lang,setOrderBy(orderBy) , setSortOrder(sortOrder) , size, pageNumber, userService.tryToGetCurrentUserId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber) {
        return mediaDao.getMediaInMoovieList(moovieListId, size, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public Media getMediaById(int mediaId) {
        Media toReturn = mediaDao.getMediaById(mediaId).orElseThrow(() -> new MediaNotFoundException("Media was not found for the id:" + mediaId));
        toReturn.setWatchlist(mediaDao.getWatchlistStatus(mediaId, userService.tryToGetCurrentUserId()));
        toReturn.setWatched(mediaDao.getWatchedStatus(mediaId, userService.tryToGetCurrentUserId()));
        return toReturn;
    }

    @Transactional(readOnly = true)
    @Override
    public Movie getMovieById(int mediaId) {
        Movie toReturn = mediaDao.getMovieById(mediaId).orElseThrow(() -> new MediaNotFoundException("Movie was not found for the id:" + mediaId));
        toReturn.setWatchlist(mediaDao.getWatchlistStatus(mediaId, userService.tryToGetCurrentUserId()));
        toReturn.setWatched(mediaDao.getWatchedStatus(mediaId, userService.tryToGetCurrentUserId()));
        return toReturn;

    }

    @Transactional(readOnly = true)
    @Override
    public TVSerie getTvById(int mediaId) {
        TVSerie toReturn = mediaDao.getTvById(mediaId).orElseThrow(() -> new MediaNotFoundException("Tv was not found for the id:" + mediaId));
        toReturn.setWatchlist(mediaDao.getWatchlistStatus(mediaId, userService.tryToGetCurrentUserId()));
        toReturn.setWatched(mediaDao.getWatchedStatus(mediaId, userService.tryToGetCurrentUserId()));
        return toReturn;
    }

    @Transactional(readOnly = true)
    @Override
    public int getDirectorsForQueryCount(String query, int size) {
        return mediaDao.getDirectorsForQueryCount(query, size);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Director> getDirectorsForQuery(String query, int size) {
        return mediaDao.getDirectorsForQuery(query, size);
    }


    @Transactional(readOnly = true)
    @Override
    public int getMediaCount(int type, String search, String participant, List<Integer> genres, List<Integer> providers, List<String> status, List<String> lang) {
        return mediaDao.getMediaCount(type, search, participant,  genres,  providers, status,  lang);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Movie> getMediaForDirectorId(int directorId) {
        return mediaDao.getMediaForDirectorId(directorId, userService.tryToGetCurrentUserId());
    }

    @Transactional(readOnly = true)
    @Override
    public boolean getWatchlistStatus(int mediaId, int userId) {
        return mediaDao.getWatchlistStatus(mediaId,userId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean getWatchedStatus(int mediaId, int userId) {
        return mediaDao.getWatchedStatus(mediaId,userId);
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

    private String setOrderBy(String orderBy){
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
}