import api from "./api";

const genreApi = (() => {

    const getAllGenres = () => {
        return api.get('/genres');
    }

    const getGenresForMedia = (id) =>{
        return api.get(`/genres`,
            {
                params: {
                    mediaId: id
                }
            }
        );
    }

    return{
        getAllGenres,
        getGenresForMedia
    }
})();

export default genreApi;