import api from './api.js';
import VndType from "../enums/VndType";

const commentApi = (() => {

    const createReviewComment = async (reviewId, comment) => {
        const response = await api.post('/comments/',
            {
                commentContent: comment
            },
            {
                params: {
                    'reviewId': reviewId
                },
                headers: {
                    'Content-Type': VndType.APPLICATION_COMMENT_FORM
                }
            }
            );
        return response;
    }

    const getReviewComments = async (reviewId,pageNumber=1,) => {
        const response =  api.get('/comments/',
            {
                params: {
                    'reviewId': reviewId,
                    'pageNumber': pageNumber
                }
            }
            );
        return response;
    }

    const commentFeedback = async (commentId, feedback) => {
        const response = await api.put('/comments/' + commentId,
            {
                feedbackType: feedback
            },
            {
                headers: {
                    'Content-Type': VndType.APPLICATION_COMMENT_FEEDBACK_FORM
                }
            }
            );
        return response
    }



    const deleteComment = async (commentId) => {
        const response = await api.delete('/comments/' + commentId);
        return response;    
    }

    return {
        deleteComment,
        createReviewComment,
        commentFeedback,
        getReviewComments
    }

})();

export default commentApi;
