package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.TV.TVCreators;

import java.util.List;

public interface TVCreatorsService {
    List<TVCreators> getTvCreatorsByMediaId(int mediaId);
    TVCreators getTvCreatorById(int creatorId);
    List<TVCreators> getTVCreatorsForQuery(String query, int size);
    List<Media> getMediasForTVCreator(int creatorId);
}
