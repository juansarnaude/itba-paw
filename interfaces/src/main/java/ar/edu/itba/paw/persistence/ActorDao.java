package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Media.Media;

import java.util.List;
import java.util.Optional;

public interface ActorDao {
    List<Actor> getAllActorsForMedia(int mediaId);//fijarse qué hacer con tvId y movieId
    Optional<Actor> getActorById(int actorId);

    int getActorsForQueryCount(String query);
    List<Actor> getActorsForQuery(String query);
    List<Media> getMediaForActor(int actorId, int currentUser);
}
