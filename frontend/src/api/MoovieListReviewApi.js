import api from './api.js'
import VndType from "../enums/VndType";

const moovieListReviewApi = (()=> {



    const getMoovieListReviewById = (id) => {
        return api.get(`/moovieListReviews/${id}`);
    }

    const getMoovieListReviewsByListId = (id,page=1) => {
        return api.get(`/moovieListReviews`,
            {params:
                    {
                        listId: id,
                        pageNumber: page
                    }
            });
    }

    const getMoovieListReviewsFromUserId = (userId,page=1) => {
        return api.get(`/moovieListReviews`,
            {
                params:{
                    'userId': userId,
                    'pageNumber': page,
                }
            });
    }

    const editReview = (id,reviewContent) => {
        return api.put(`/moovieListReviews`,
            {
                reviewContent: reviewContent
            },
        {
                params:{
                    'listId': id
                },
                headers: {
                    'Content-Type': VndType.APPLICATION_MOOVIELIST_REVIEW_FORM,
                },
            }
            );
    }

    const createMoovieListReview = (id,reviewContent) => {
        return api.post(`/moovieListReviews`,
            {
                reviewContent : reviewContent
            },
            {params:
                    {
                        listId:id,
                    },
                headers: {
                    'Content-Type': VndType.APPLICATION_MOOVIELIST_REVIEW_FORM,
                },
            });
    }

    const deleteMoovieListReviewById = (id) => {
        return api.delete(`/moovieListReviews/${id}`);
    }

    const likeMoovieListReview = (username, id) => {
        return api.put(`/moovieListReviews/${id}`,
            {"username": username,
                "feedbackType" : "LIKE"},
            {headers: {
                    'Content-Type': VndType.APPLICATION_MOOVIELIST_FEEDBACK_FORM
                }
            });
    }


    return {
        getMoovieListReviewById,
        getMoovieListReviewsByListId,
        getMoovieListReviewsFromUserId,
        editReview,
        createMoovieListReview,
        deleteMoovieListReviewById,
        likeMoovieListReview
    }

})();

export default moovieListReviewApi;