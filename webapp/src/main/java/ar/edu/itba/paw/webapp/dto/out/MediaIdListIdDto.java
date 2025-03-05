package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.OrderedMedia;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

public class MediaIdListIdDto {
    private int mediaId;
    private String mediaUrl;
    private int moovieListId;
    private String listUrl;
    private int customOrder;

    public MediaIdListIdDto() {
    }

    public MediaIdListIdDto(int mediaId, int moovieListId, int customOrder, UriInfo uriInfo) {
        this.mediaId = mediaId;
        this.mediaUrl = uriInfo.getBaseUriBuilder().path("medias/{mediaId}").build(mediaId).toString();
        this.moovieListId = moovieListId;
        this.listUrl = uriInfo.getBaseUriBuilder().path("lists/{moovieListId}").build(moovieListId).toString();
        this.customOrder = customOrder;
    }

    public static List<MediaIdListIdDto> fromOrderedMediaList(List<OrderedMedia> medias, int moovieListId, UriInfo uriInfo){
        List<MediaIdListIdDto> toRet = new ArrayList<>();
        for (OrderedMedia media : medias) {
            toRet.add(new MediaIdListIdDto(media.getMedia().getMediaId(), moovieListId, media.getCustomOrder(), uriInfo));
        }
        return toRet;
    }

    public int getMediaId() {
        return mediaId;
    }

    public int getMoovieListId() {
        return moovieListId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setMoovieListId(int moovieListId) {
        this.moovieListId = moovieListId;
    }

    public int getCustomOrder() {
        return customOrder;
    }

    public void setCustomOrder(int customOrder) {
        this.customOrder = customOrder;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getListUrl() {
        return listUrl;
    }

    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }
}
