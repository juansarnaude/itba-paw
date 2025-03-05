import castApi from "../api/CastApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";

const CastService = (() => {

    const getActorsForQuery = async (search) => {
        return await castApi.getActorsForQuery(search);
    }

    const getActorsByMediaId = async (mediaId) =>{
        const res = await castApi.getActorsByMediaId(mediaId);
        return parsePaginatedResponse(res);
    }

    const getActorById = async (id) => {
        return await castApi.getActorById(id);
    }

    const getDirectorsForQuery = async (search) => {
        return await castApi.getDirectorsForQuery(search);
    }

    const getTvCreatorById = async (id) => {
        return await castApi.getTvCreatorById(id);
    }

    const getTvCreatorsSearch = async (search) => {
        return await castApi.getTvCreatorsSearch(search);
    }

    const getTvCreatorsByMediaId = async (mediaId) =>{
        const res = await castApi.getTvCreatorsByMediaId(mediaId);
        return parsePaginatedResponse(res);
    }

    return{
        getActorsForQuery,
        getActorById,
        getActorsByMediaId,
        getDirectorsForQuery,
        getTvCreatorById,
        getTvCreatorsSearch,
        getTvCreatorsByMediaId
    }
})();

export default CastService;