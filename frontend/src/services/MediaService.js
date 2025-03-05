import mediaApi from "../api/MediaApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";

const MediaService = (() => {
    const getMedia = async ({type, page, pageSize, orderBy, sortOrder, search, providers, genres}) => {
        const res = await mediaApi.getMedia({type, page, pageSize, orderBy, sortOrder, search, providers, genres});
        return parsePaginatedResponse(res);
    }

    const getProvidersForMedia = async ({url}) => {
        const res = await mediaApi.getProvidersForMedia(url);
        return res;
    }

    const getMediaById = async (id) => {
        const res = await mediaApi.getMediaById(id);
        return parsePaginatedResponse(res);
    }

    const getMediaByIdList = async (idList) => {
        const res = await mediaApi.getMediaByIdList(idList);
        return res;
    }




    const getIdMediaFromObjectList = (list) => {
        let toRet = "";
        for (const m of list) {
            toRet += m.mediaId + ",";
        }
        return toRet.slice(0, -1); // Removes the last comma
    };

    const getMediasForTVCreator = async (id) => {
        return await mediaApi.getMediasForTVCreator(id);
    }

    const getMediasForDirector = async (id) => {
        return await mediaApi.getMediasForDirector(id);
    }

    const getMediasForActor = async (id) => {
        return await mediaApi.getMediasForActor(id);
    }


    return {
        getMedia,
        getProvidersForMedia,
        getMediaById,
        getMediaByIdList,
        getIdMediaFromObjectList,
        getMediasForTVCreator,
        getMediasForDirector,
        getMediasForActor
    }
})();

export default MediaService;