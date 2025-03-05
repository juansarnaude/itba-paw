import api from './api'
import VndType from "../enums/VndType";

const listApi = (() => {

    const getLists = ({search, ownerUsername, type, orderBy, order, pageNumber, pageSize}) =>{
        return api.get('lists',
            {
                params:{
                    'search' : search,
                    'ownerUsername': ownerUsername,
                    'type': type,
                    'orderBy': orderBy,
                    'order': order,
                    'pageNumber': pageNumber,
                    'pageSize': pageSize
                }
        });
    }

    const getListById = (id) => {
        return api.get( `/lists/${id}`);
    }

    const getListByIdList = (idListString) => {
        return api.get(`/lists?ids=${idListString}`);
    };


    const getListContentById= ({id, orderBy, sortOrder, pageNumber, pageSize}) => {
        return api.get(`/lists/${id}/content`,
            {
                params: {
                    'orderBy': orderBy,
                    'sortOrder': sortOrder,
                    'pageNumber': pageNumber,
                    'pageSize': pageSize
                }
            });
    }

    const deleteList = (id) => {
        return api.delete(`/lists/${id}`);
    }




    const insertMediaIntoMoovieList = ({ id, mediaIds }) => {
        return api.post(
            `/lists/${id}/content`,
            { mediaIdList: mediaIds },  // Rename `mediaIds` to `mediaIdList`
            {
                headers: {
                    'Content-Type': VndType.APPLICATION_MOOVIELIST_MEDIA_FORM,
                },
            }
        );
    };

    const deleteMediaFromMoovieList = ({ id, mediaId }) => {
        return api.delete(
            `/lists/${id}/content/${mediaId}`
        );
    };

    //PUT

    const editMoovieList = async (mlId, name, description) => {
        const form = {
            listName: name,
            listDescription: description,
        };
        const response = await api.put('/lists/' + mlId,
            form,{
                headers: {
                    'Content-Type': VndType.APPLICATION_MOOVIELIST_FORM
                }
            });
        return response;
    }



    const getRecommendedLists =  (id) => {
        return api.get(`/lists/${id}/recommendedLists`,
            {
                params:{
                    'id': id
                }
            })
    }

    const getMediaFromList = ( listId, mediaId) => {
        return api.get(`/lists/${listId}/content/${mediaId}`);
    }

    const editListContent =  (listId, mediaId, customOrder) => {
        const input = {
            mediaId: mediaId,
            moovieListId: listId,
            customOrder: customOrder,

        };

        const response =  api.put(`lists/${listId}/content/${mediaId}`,
            input,{
                headers: {
                    'Content-Type': VndType.APPLICATION_MOOVIELIST_MEDIA_FORM,
                }
            }
        );
        return response;
    }

    // POST

    const createMoovieList = (name, type, description) => {
        const body = {
            name: name,
            type: type,
            description: description
        }
        return api.post('lists', body,{
            headers: {
                'Content-Type': VndType.APPLICATION_MOOVIELIST_FORM
            }
        })
    }

    const likeList = (moovieListId, username) =>{
        return api.put(`/lists/${moovieListId}`,
            {"username": username,
                "feedbackType" : "LIKE"},
            {headers: {
                    'Content-Type': VndType.APPLICATION_MOOVIELIST_FEEDBACK_FORM
                }
            });
    }

    const unlikeList = (moovieListId, username) =>{
        return api.put(`/lists/${moovieListId}`,
            {"username": username,
                "feedbackType" : "UNLIKE"},
            {headers: {
                    'Content-Type': VndType.APPLICATION_MOOVIELIST_FEEDBACK_FORM
                }
            });
    }

    const followList = (moovieListId, username) =>{
        return api.put(`/lists/${moovieListId}`,
            {"username":username,
                "actionType": "FOLLOW"},

            {headers: {
                    'Content-Type': VndType.APPLICATION_MOOVIELIST_FOLLOW_FORM
                }
            });
    }

    const unfollowList = (moovieListId, username) =>{
        return api.put(`/lists/${moovieListId}`,
            {"username":username,
                "actionType": "UNFOLLOW"},

            {headers: {
                    'Content-Type': VndType.APPLICATION_MOOVIELIST_FOLLOW_FORM
                }
            });
    }

    return{
        getLists,
        getListById,
        getListByIdList,
        deleteList,
        getListContentById,
        insertMediaIntoMoovieList,
        deleteMediaFromMoovieList,
        editMoovieList,
        getRecommendedLists,
        editListContent,
        createMoovieList,
        likeList,
        unlikeList,
        followList,
        unfollowList,
        getMediaFromList
    }
})();

export default listApi;