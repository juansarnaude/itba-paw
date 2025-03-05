import reportApi from '../../api/reportApi';
import api from '../../api/api.js';
import VndType from "../../enums/VndType";

jest.mock('../../api/api.js');

describe('reportApi', () => {

    afterEach(() => {
        jest.clearAllMocks();
    });

    // --------------- REPORTING TESTS ---------------
    test('reportReview should send a POST request with correct data', async () => {
        const reportData = { reviewId: 123, reportedBy: "user1", content: "Spam", type: "SPAM" };
        const mockResponse = { data: { message: 'Review reported' } };

        api.post.mockResolvedValue(mockResponse);

        const response = await reportApi.reportReview(reportData);

        expect(api.post).toHaveBeenCalledWith('/reports',
            {
                reportedBy: reportData.reportedBy,
                content: reportData.content,
                type: reportData.type
            },
            {
                params: { reviewId: reportData.reviewId },
                headers: { 'Content-Type': VndType.APPLICATION_REPORT_FORM }
            }
        );

        expect(response).toEqual(mockResponse);
    });

    test('reportComment should send a POST request with correct data', async () => {
        const reportData = { commentId: 456, reportedBy: "user2", content: "Offensive", type: "OFFENSIVE" };
        const mockResponse = { data: { message: 'Comment reported' } };

        api.post.mockResolvedValue(mockResponse);

        const response = await reportApi.reportComment(reportData);

        expect(api.post).toHaveBeenCalledWith('/reports',
            {
                commentId: reportData.commentId,
                reportedBy: reportData.reportedBy,
                content: reportData.content,
                type: reportData.type
            },
            {
                params: { commentId: reportData.commentId },
                headers: { 'Content-Type': VndType.APPLICATION_REPORT_FORM }
            }
        );

        expect(response).toEqual(mockResponse);
    });

    test('reportMoovieList should send a POST request with correct data', async () => {
        const reportData = { moovieListId: 789, reportedBy: "user3", content: "Copyright Issue", type: "COPYRIGHT" };
        const mockResponse = { data: { message: 'Moovie list reported' } };

        api.post.mockResolvedValue(mockResponse);

        const response = await reportApi.reportMoovieList(reportData);

        expect(api.post).toHaveBeenCalledWith('/reports',
            {
                moovieListId: reportData.moovieListId,
                reportedBy: reportData.reportedBy,
                content: reportData.content,
                type: reportData.type
            },
            {
                params: { moovieListId: reportData.moovieListId },
                headers: { 'Content-Type': VndType.APPLICATION_REPORT_FORM }
            }
        );

        expect(response).toEqual(mockResponse);
    });

    test('reportMoovieListReview should send a POST request with correct data', async () => {
        const reportData = { moovieListReviewId: 101, reportedBy: "user4", content: "Harassment", type: "HARASSMENT" };
        const mockResponse = { data: { message: 'Moovie list review reported' } };

        api.post.mockResolvedValue(mockResponse);

        const response = await reportApi.reportMoovieListReview(reportData);

        expect(api.post).toHaveBeenCalledWith('/reports',
            {
                moovieListReviewId: reportData.moovieListReviewId,
                reportedBy: reportData.reportedBy,
                content: reportData.content,
                type: reportData.type
            },
            {
                params: { moovieListReviewId: reportData.moovieListReviewId },
                headers: { 'Content-Type': VndType.APPLICATION_REPORT_FORM }
            }
        );

        expect(response).toEqual(mockResponse);
    });

    // --------------- GET REPORTS TEST ---------------
    test('getReports should send a GET request with correct params', async () => {
        const contentType = "COMMENTS";
        const mockResponse = { data: [{ id: 1, content: "Reported comment" }] };

        api.get.mockResolvedValue(mockResponse);

        const response = await reportApi.getReports({ contentType });

        expect(api.get).toHaveBeenCalledWith('/reports', { params: { contentType } });
        expect(response).toEqual(mockResponse);
    });

    // --------------- RESOLVE REPORTS TESTS ---------------
    test('resolveReviewReport should send a DELETE request with correct params', async () => {
        const reviewId = 202;
        const mockResponse = { data: { message: 'Review report resolved' } };

        api.delete.mockResolvedValue(mockResponse);

        const response = await reportApi.resolveReviewReport(reviewId);

        expect(api.delete).toHaveBeenCalledWith('/reports', { params: { reviewId } });
        expect(response).toEqual(mockResponse);
    });

    test('resolveCommentReport should send a DELETE request with correct params', async () => {
        const commentId = 303;
        const mockResponse = { data: { message: 'Comment report resolved' } };

        api.delete.mockResolvedValue(mockResponse);

        const response = await reportApi.resolveCommentReport(commentId);

        expect(api.delete).toHaveBeenCalledWith('/reports', { params: { commentId } });
        expect(response).toEqual(mockResponse);
    });

    test('resolveMoovieListReport should send a DELETE request with correct params', async () => {
        const moovieListId = 404;
        const mockResponse = { data: { message: 'Moovie list report resolved' } };

        api.delete.mockResolvedValue(mockResponse);

        const response = await reportApi.resolveMoovieListReport(moovieListId);

        expect(api.delete).toHaveBeenCalledWith('/reports', { params: { moovieListId } });
        expect(response).toEqual(mockResponse);
    });

    test('resolveMoovieListReviewReport should send a DELETE request with correct params', async () => {
        const moovieListReviewId = 505;
        const mockResponse = { data: { message: 'Moovie list review report resolved' } };

        api.delete.mockResolvedValue(mockResponse);

        const response = await reportApi.resolveMoovieListReviewReport(moovieListReviewId);

        expect(api.delete).toHaveBeenCalledWith('/reports', { params: { moovieListReviewId } });
        expect(response).toEqual(mockResponse);
    });

});
