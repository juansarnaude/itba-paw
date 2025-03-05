package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Genre.Genre;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

public class GenreDto {
    private int genreId;
    private String genreName;
    private String url;

    public static GenreDto fromGenre(Genre genre, UriInfo uriInfo){
        GenreDto genreDto = new GenreDto();
        genreDto.genreId = genre.getId();
        genreDto.genreName = genre.getGenre();
        genreDto.url = uriInfo.getBaseUriBuilder().path("genres/{genreId}").build(genre.getId()).toString();
        return genreDto;
    }

    public static List<GenreDto> fromGenreList(List<Genre> genreList, UriInfo uriInfo){
        return genreList.stream().map(m -> fromGenre(m,uriInfo)).collect(Collectors.toList());
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
