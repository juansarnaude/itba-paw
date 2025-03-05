import listApi from "../api/ListApi";
import {parsePaginatedResponse} from "../utils/ResponseUtils";
import mediaService from "./MediaService";
import profileApi from "../api/ProfileApi";

const ListService = (() => {

    const getLists = async ({search, ownerUsername, type, orderBy, order, pageNumber, pageSize}) =>{
        const res = await listApi.getLists({search, ownerUsername, type, orderBy, order, pageNumber, pageSize});
        return parsePaginatedResponse(res);
    }

    const getListById = async (id) => {
        const res = await listApi.getListById(id);
        return parsePaginatedResponse(res);
    }

    const getListByIdList = async (idList) => {
        const res = await listApi.getListByIdList(idList);
        return res;
    }

    const editMoovieList = async ({id, name, description}) =>{
        const res= await listApi.editMoovieList(id, name.trim(), description.trim());
        return res;
    }

    const getIdListFromObjectList = (list) => {
        let toRet = "";
        for (const m of list) {
            toRet += m.mlId + ",";
        }
        return toRet.slice(0, -1); // Removes the last comma
    };


    const getListContentById= async ({id, orderBy, sortOrder, pageNumber, pageSize}) => {
        const res = await listApi.getListContentById({id, orderBy, sortOrder, pageNumber, pageSize});
        const contentList = parsePaginatedResponse(res).data;
        const contentListLinks = parsePaginatedResponse(res).links;
        
        if (contentList.length === 0) {
            return {
                data: [],
                links: contentListLinks
            };
        }

        const toRetMedia = await mediaService.getMediaByIdList(mediaService.getIdMediaFromObjectList(contentList));
        for( let i= 0 ; i < contentList.length ; i++ ){
            toRetMedia.data[i].customOrder =  contentList[i].customOrder;
        }
        toRetMedia.links = contentListLinks;
        return toRetMedia;
    }

    const createMoovieList = async  ({name, type, description}) => {
        return await listApi.createMoovieList(name,type,description)
    }

    const editListContent = async ({listId, mediaId, customOrder}) => {
        return listApi.editListContent(listId, mediaId, customOrder);
    }

    const insertMediaIntoMoovieList = async ({id, mediaIds}) => {
        const res = await listApi.insertMediaIntoMoovieList({id,mediaIds});
        return res;
    }

    const deleteMediaFromMoovieList = async ({id, mediaId}) =>{
        const res = await listApi.deleteMediaFromMoovieList({id, mediaId});
        return res;
    }



    const getRecommendedLists = async (id) => {
        return await listApi.getRecommendedLists(id);
    }


    const likeList = async (moovieListId, username) => {
        try {
            return await listApi.likeList(moovieListId, username)
        } catch (error){
            return null;
        }
    }

    const unlikeList = async (moovieListId, username) => {
        try {
            return await listApi.unlikeList(moovieListId, username)
        } catch (error){
            return null;
        }
    }

    const followList = async (moovieListId, username) => {
        try {
            return await listApi.followList(moovieListId, username)
        } catch (error){
            return null;
        }
    }

    const unfollowList = async (moovieListId, username) => {
        try {
            return await listApi.unfollowList(moovieListId, username)
        } catch (error){
            return null;
        }
    }

   return{
        getLists,
        getListById,
        getListContentById,
        getListByIdList,
        getIdListFromObjectList,
        insertMediaIntoMoovieList,
       deleteMediaFromMoovieList,
       editMoovieList,
       getRecommendedLists,
       editListContent,
       createMoovieList,
       likeList,
       unlikeList,
       followList,
       unfollowList
   }
})();

export default ListService;