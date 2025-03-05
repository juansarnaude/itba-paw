package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.TVCreatorNotFoundException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.TV.TVCreators;
import ar.edu.itba.paw.persistence.TVCreatorsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TVCreatorsServiceImpl implements TVCreatorsService{
    @Autowired
    private TVCreatorsDao tvCreatorsDao;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    @Override
    public List<TVCreators> getTvCreatorsByMediaId(int mediaId) {
        return tvCreatorsDao.getTvCreatorsByMediaId(mediaId);
    }

    @Transactional(readOnly = true)
    @Override
    public TVCreators getTvCreatorById(int creatorId) {
        TVCreators toReturn = tvCreatorsDao.getTvCreatorById(creatorId).orElseThrow( () -> new TVCreatorNotFoundException("TVCreator was not found for the id: " + creatorId));

        int currentUserId = userService.tryToGetCurrentUserId();

        if ( currentUserId >= 0 ){
            for(Media m : toReturn.getMedias()){
                m.setWatched(mediaService.getWatchedStatus(m.getMediaId(), currentUserId));
                m.setWatchlist(mediaService.getWatchlistStatus(m.getMediaId(),currentUserId));
            }
        }

        return toReturn;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Media> getMediasForTVCreator(int creatorId) {
        return tvCreatorsDao.getMediasForTVCreator(creatorId, userService.tryToGetCurrentUserId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<TVCreators> getTVCreatorsForQuery(String query, int size) {
        List<TVCreators> toReturn = tvCreatorsDao.getTVCreatorsForQuery(query, size);

        int currentUserId = userService.tryToGetCurrentUserId();

        if(currentUserId >= 0){
            for( TVCreators tv : toReturn ){
                for(Media m : tv.getMedias()){
                    m.setWatched(mediaService.getWatchedStatus(m.getMediaId(), currentUserId));
                    m.setWatchlist(mediaService.getWatchlistStatus(m.getMediaId(),currentUserId));
                }
            }
        }


        return toReturn;
    }


}
