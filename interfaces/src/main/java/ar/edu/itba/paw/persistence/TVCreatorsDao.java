package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.TV.TVCreators;

import java.util.List;
import java.util.Optional;

public interface TVCreatorsDao {
    List<TVCreators> getTvCreatorsByMediaId(int mediaId);
    Optional<TVCreators> getTvCreatorById(int creatorId);
    List<TVCreators> getTVCreatorsForQuery(String query, int size);
    List<Media> getMediasForTVCreator(int creatorId, int currentUser);
}
