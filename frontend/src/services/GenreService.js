import genreApi from "../api/GenreApi";

const GenreService = (() => {

    const getAllGenres = async () => {
        return await genreApi.getAllGenres();
    }

    const getGenresForMedia = async (id) =>{
        return await genreApi.getGenresForMedia(id);
    }

    return{
        getAllGenres,
        getGenresForMedia
    }
})();

export default GenreService;