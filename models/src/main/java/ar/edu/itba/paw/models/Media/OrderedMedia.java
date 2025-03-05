package ar.edu.itba.paw.models.Media;


public class OrderedMedia extends Media{
    private Media media;
    private int customOrder;

    public OrderedMedia(){

    }

    public OrderedMedia(Media media, int customOrder) {
        this.media = media;
        this.customOrder = customOrder;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public int getCustomOrder() {
        return customOrder;
    }

    public void setCustomOrder(int customOrder) {
        this.customOrder = customOrder;
    }
}
