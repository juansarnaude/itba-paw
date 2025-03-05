package ar.edu.itba.paw.models.Cast;

public class Director {
    private int directorId;

    private String name;

    private long totalMedia;

    public Director(int directorId, String name, long totalMedia) {
        this.totalMedia = totalMedia;
        this.directorId = directorId;
        this.name = name;
    }

    public long getTotalMedia() {
        return totalMedia;
    }

    public int getDirectorId() {
        return directorId;
    }

    public String getName() {
        return name;
    }
}
