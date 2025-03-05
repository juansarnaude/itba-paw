package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.ActorNotFoundException;
import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.persistence.ActorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActorServiceImpl implements ActorService{
    @Autowired
    private ActorDao actorDao;

    @Autowired
    private UserService userService;

    @Autowired
    private MediaService mediaService;

    @Transactional(readOnly = true)
    @Override
    public List<Actor> getAllActorsForMedia(int mediaId) {
        return actorDao.getAllActorsForMedia(mediaId);
    }

    @Transactional(readOnly = true)
    @Override
    public Actor getActorById(int actorId) {
        Actor toReturn = actorDao.getActorById(actorId).orElseThrow( () -> new ActorNotFoundException("Actor was not found for the id: " + actorId));

        int currentUserId = userService.tryToGetCurrentUserId();

        try{
            if(currentUserId >= 0 ){
                for(Media m : toReturn.getMedias()){
                    m.setWatchlist(mediaService.getWatchlistStatus(m.getMediaId(),currentUserId));
                    m.setWatched(mediaService.getWatchedStatus(m.getMediaId(),currentUserId));
                }
            }
        }catch(Exception e){
            throw new ActorNotFoundException("There was an error setting up the actor media");
        }


        return toReturn;
    }

    @Transactional(readOnly = true)
    @Override
    public int getActorsForQueryCount(String query) {
        return actorDao.getActorsForQueryCount(query);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Actor> getActorsForQuery(String query) {
        return actorDao.getActorsForQuery(query);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Media> getMediaForActor(int actorId) {
        return actorDao.getMediaForActor(actorId, userService.tryToGetCurrentUserId());
    }

}
