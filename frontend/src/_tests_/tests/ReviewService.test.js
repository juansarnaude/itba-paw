import ReviewService from '../../services/ReviewService';
import reviewApi from '../../api/ReviewApi';

jest.mock('../../api/ReviewApi');

describe('ReviewService', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });

    test('should fetch review by id', async () => {
        const mockReview = { id: 1, rating: 5, reviewContent: 'Great movie!' };
        reviewApi.getReviewById.mockResolvedValue(mockReview);

        const result = await ReviewService.getReviewById(1);

        expect(reviewApi.getReviewById).toHaveBeenCalledWith(1);
        expect(result).toEqual(mockReview);
    });

    test('should fetch reviews by media id', async () => {
        const mockReviews = { data: [{ id: 1, rating: 5, reviewContent: 'Great movie!' }], links: null };
        reviewApi.getReviewsByMediaId.mockResolvedValue(mockReviews);

        const result = await ReviewService.getReviewsByMediaId(123);

        expect(reviewApi.getReviewsByMediaId).toHaveBeenCalledWith(123);
        expect(result).toEqual(mockReviews);
    });

    test('should fetch reviews by media id and user id', async () => {
        const mockReviews = { data: [{ id: 1, rating: 5, reviewContent: 'Great movie!' }] };
        reviewApi.getReviewsByMediaIdandUserId.mockResolvedValue(mockReviews);

        const result = await ReviewService.getReviewsByMediaIdandUserId(123, 456);

        expect(reviewApi.getReviewsByMediaIdandUserId).toHaveBeenCalledWith(123, 456);
        expect(result).toEqual(mockReviews);
    });

    test('should fetch movie reviews from user', async () => {
        // Mock the response that comes from the API
        const mockReviews = { data: [{ id: 1, rating: 5, reviewContent: 'Great movie!' }], links: null };

        // Mock the API call to return this mock data
        reviewApi.getMovieReviewsFromUser.mockResolvedValue(mockReviews);

        // Mock parsePaginatedResponse (if it's transforming the response)
        jest.mock('../../utils/ResponseUtils', () => ({
            parsePaginatedResponse: jest.fn().mockReturnValue(mockReviews)
        }));

        const result = await ReviewService.getMovieReviewsFromUser('john_doe', 1);

        // Make sure the correct API method was called
        expect(reviewApi.getMovieReviewsFromUser).toHaveBeenCalledWith('john_doe', 1);

        // Ensure that the transformed result is as expected
        expect(result).toEqual(mockReviews);
    });

    test('should edit a review', async () => {
        const mockEditedReview = { id: 1, rating: 5, reviewContent: 'Amazing movie!' };
        reviewApi.editReview.mockResolvedValue(mockEditedReview);

        const result = await ReviewService.editReview(123, 5, 'Amazing movie!');

        expect(reviewApi.editReview).toHaveBeenCalledWith({ mediaId: 123, rating: 5, reviewContent: 'Amazing movie!' });
        expect(result).toEqual(mockEditedReview);
    });

    test('should create a review', async () => {
        const mockNewReview = { id: 1, rating: 4, reviewContent: 'Good movie!' };
        reviewApi.createReview.mockResolvedValue(mockNewReview);

        const result = await ReviewService.createReview(123, 4, 'Good movie!');

        expect(reviewApi.createReview).toHaveBeenCalledWith({ mediaId: 123, rating: 4, reviewContent: 'Good movie!' });
        expect(result).toEqual(mockNewReview);
    });

    test('should delete review by id', async () => {
        const mockDeleteResponse = { status: 200 };
        reviewApi.deleteReviewById.mockResolvedValue(mockDeleteResponse);

        const result = await ReviewService.deleteReviewById(1);

        expect(reviewApi.deleteReviewById).toHaveBeenCalledWith(1);
        expect(result).toEqual(mockDeleteResponse);
    });

    test('should like a review', async () => {
        const mockLikeResponse = { status: 200 };
        reviewApi.likeReview.mockResolvedValue(mockLikeResponse);

        const result = await ReviewService.likeReview(1);

        expect(reviewApi.likeReview).toHaveBeenCalledWith(1);
        expect(result).toEqual(mockLikeResponse);
    });
});
