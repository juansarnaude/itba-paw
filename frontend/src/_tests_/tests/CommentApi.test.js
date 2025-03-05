import commentApi from '../../api/CommentApi.js';
import api from '../../api/api.js';
import VndType from "../../enums/VndType";

jest.mock('../../api/api.js');

describe('commentApi', () => {

    afterEach(() => {
        jest.clearAllMocks();
    });

    test('createReviewComment should send a POST request with correct data', async () => {
        const reviewId = 123;
        const comment = "This is a test comment";
        const mockResponse = { data: { message: 'Comment created' } };

        api.post.mockResolvedValue(mockResponse);

        const response = await commentApi.createReviewComment(reviewId, comment);

        expect(api.post).toHaveBeenCalledWith('/comments/', {
            commentContent: comment
        }, {
            params: { 'reviewId': reviewId },
            headers: { 'Content-Type': VndType.APPLICATION_COMMENT_FORM }
        });

        expect(response).toEqual(mockResponse);
    });

    test('getReviewComments should send a GET request with correct params', async () => {
        const reviewId = 123;
        const pageNumber = 2;
        const mockResponse = { data: [{ id: 1, content: "Test comment" }] };

        api.get.mockResolvedValue(mockResponse);

        const response = await commentApi.getReviewComments(reviewId, pageNumber);

        expect(api.get).toHaveBeenCalledWith('/comments/', {
            params: { 'reviewId': reviewId, 'pageNumber': pageNumber }
        });

        expect(response).toEqual(mockResponse);
    });

    test('commentFeedback should send a PUT request with correct data', async () => {
        const commentId = 456;
        const feedback = "Like";
        const mockResponse = { data: { message: 'Feedback added' } };

        api.put.mockResolvedValue(mockResponse);

        const response = await commentApi.commentFeedback(commentId, feedback);

        expect(api.put).toHaveBeenCalledWith(`/comments/${commentId}`, {
            feedback: feedback
        }, {
            headers: { 'Content-Type': VndType.APPLICATION_COMMENT_FEEDBACK_FORM }
        });

        expect(response).toEqual(mockResponse);
    });

    test('deleteComment should send a DELETE request with correct URL', async () => {
        const commentId = 789;
        const mockResponse = { data: { message: 'Comment deleted' } };

        api.delete.mockResolvedValue(mockResponse);

        const response = await commentApi.deleteComment(commentId);

        expect(api.delete).toHaveBeenCalledWith(`/comments/${commentId}`);
        expect(response).toEqual(mockResponse);
    });

});
