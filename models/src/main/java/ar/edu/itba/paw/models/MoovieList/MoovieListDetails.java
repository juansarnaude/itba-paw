package ar.edu.itba.paw.models.MoovieList;

import ar.edu.itba.paw.models.Media.Media;

import java.util.List;

public class MoovieListDetails {
    private final MoovieListCard card;
    private final List<Media> content;

    public MoovieListDetails(MoovieListCard card, List<Media> content) {
        this.card = card;
        this.content = content;
    }

    public MoovieListCard getCard() {
        return card;
    }

    public List<Media> getContent() {
        return content;
    }


}
