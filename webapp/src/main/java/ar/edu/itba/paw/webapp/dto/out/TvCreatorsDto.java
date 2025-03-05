package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.TV.TVCreators;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class TvCreatorsDto {

    private int id;

    private String creatorName;
    
    
    private String url;
    
    private String [] mediasUrls;

    public static TvCreatorsDto fromTvCreator(final TVCreators tvCreator, final UriInfo uriInfo) {
        final TvCreatorsDto dto = new TvCreatorsDto();
        
        dto.creatorName=tvCreator.getCreatorName();
        dto.id=tvCreator.getCreatorId();
        
        dto.url=uriInfo.getBaseUriBuilder().path("tvCreators/{id}").build(tvCreator.getCreatorId()).toString();
        dto.mediasUrls = tvCreator.getMedias().stream()
                .map(media -> uriInfo.getBaseUriBuilder()
                        .path("medias/{id}")
                        .build(media.getMediaId())
                        .toString())
                .toArray(String[]::new);
        return dto;
    }
    
    public static List<TvCreatorsDto> fromTvCreatorList(final List<TVCreators> tvCreatorList, final UriInfo uriInfo) {
        return tvCreatorList.stream().map(m -> fromTvCreator(m, uriInfo)).collect(java.util.stream.Collectors.toList());
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getMediasUrls() {
        return mediasUrls;
    }

    public void setMediasUrls(String[] mediasUrls) {
        this.mediasUrls = mediasUrls;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getCreatorName() {
        return creatorName;
    }
    
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    

    
}
