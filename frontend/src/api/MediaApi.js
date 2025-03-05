import api from './api.js'
import search from "../pages/views/search";

const mediaApi = (()=> {

    const getMedia = ({type, page, pageSize, orderBy, sortOrder, search, providers, genres}) => {
        return api.get('/medias',
            {
                params: {
                    'type': type,
                    'orderBy': orderBy,
                    'sortOrder': sortOrder,
                    'pageNumber': page,
                    'pageSize': pageSize,
                    'search': search,
                    'providers': providers,
                    'genres': genres
                }
            });
    }

    const getMediaByIdList = (idListString) => {
        return api.get(`/medias?ids=${idListString}`);
    };

    const getProvidersForMedia = (url) => {
        return api.get(url);
    }


    const getMediaById = (id) => {
        return api.get(`/medias/${id}`);
    }


    const getMediasForTVCreator = ({id}) => {
        return api.get(`/medias`,
            {
                params: {
                    'tvCreatorId': id
                }
            }
        );
    }

    const getMediasForDirector = ({id}) => {
        return api.get(`/medias`,
            {
                params: {
                    'directorId': id
                }
            }
        );
    }

    const getMediasForActor = ({id}) => {
        return api.get(`/medias`,
            {
                params: {
                    'actorId': id
                }
            }
        );
    }

    return {
        getMedia,
        getProvidersForMedia,
        getMediaById,
        getMediaByIdList,
        getMediasForTVCreator,
        getMediasForDirector,
        getMediasForActor
    }

})();

export default mediaApi;