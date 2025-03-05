package ar.edu.itba.paw.webapp.dto.in;

import javax.validation.constraints.NotNull;
import java.util.List;

public class MediaListDto {
    @NotNull
    private List<Integer> mediaIdList;

    public List<Integer> getMediaIdList() {
        return mediaIdList;
    }

    public void setMediaIdList(List<Integer> mediaIdList) {
        this.mediaIdList = mediaIdList;
    }
}