package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Media.TVSerie;
import javax.ws.rs.core.UriInfo;
import java.util.Date;

public class TVSerieDto extends MediaDto{

    private Date lastAirDate;

    private Date nextEpisodeToAir;

    private int numberOfEpisodes;

    private int numberOfSeasons;

    public static TVSerieDto fromTVSerie(TVSerie tvserie, UriInfo uri) {
        TVSerieDto tvSerieDTO = new TVSerieDto();
        MediaDto.setFromMediaChild(tvSerieDTO, tvserie, uri);
        tvSerieDTO.lastAirDate = tvserie.getLastAirDate();
        tvSerieDTO.numberOfEpisodes = tvserie.getNumberOfEpisodes();
        tvSerieDTO.numberOfEpisodes = tvserie.getNumberOfEpisodes();
        tvSerieDTO.numberOfSeasons = tvserie.getNumberOfSeasons();

        return tvSerieDTO;
    }

    public Date getLastAirDate() {
        return lastAirDate;
    }

    public void setLastAirDate(Date lastAirDate) {
        this.lastAirDate = lastAirDate;
    }

    public Date getNextEpisodeToAir() {
        return nextEpisodeToAir;
    }

    public void setNextEpisodeToAir(Date nextEpisodeToAir) {
        this.nextEpisodeToAir = nextEpisodeToAir;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }
}
