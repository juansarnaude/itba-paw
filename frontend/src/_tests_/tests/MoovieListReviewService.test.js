import MoovieListReviewService from '../../services/MoovieListReviewService';
import moovieListReviewApi from '../../api/MoovieListReviewApi';

jest.mock('../../api/MoovieListReviewApi'); // Mock the entire API module

describe('MoovieListReviewService', () => {

    afterEach(() => {
        jest.clearAllMocks(); // Clear mocks after each test to ensure a fresh state
    });

    test('should fetch moovie list review by ID', async () => {
        const mockReview = { id: 1, reviewContent: "Great movie!" };
        moovieListReviewApi.getMoovieListReviewById.mockResolvedValue({ data: mockReview });

        const result = await MoovieListReviewService.getMoovieListReview(1);

        expect(moovieListReviewApi.getMoovieListReviewById).toHaveBeenCalledWith(1);
        expect(result).toEqual({ data: mockReview });
    });

    test('should fetch moovie list reviews by list ID', async () => {
        const mockReviews = [
            { id: 1, reviewContent: "Great movie!" },
            { id: 2, reviewContent: "Not bad!" }
        ];
        moovieListReviewApi.getMoovieListReviewsByListId.mockResolvedValue({ data: mockReviews });

        const result = await MoovieListReviewService.getMoovieListReviewsByListId(1, 1);

        expect(moovieListReviewApi.getMoovieListReviewsByListId).toHaveBeenCalledWith(1, 1);
        expect(result).toEqual({ data: mockReviews });
    });

    test('should fetch moovie list reviews from user ID', async () => {
        const mockReviews = [
            { id: 1, reviewContent: "Loved it!" },
            { id: 2, reviewContent: "Not my type" }
        ];
        moovieListReviewApi.getMoovieListReviewsFromUserId.mockResolvedValue({ data: mockReviews });

        const result = await MoovieListReviewService.getMoovieListReviewsFromUserId(1, 1);

        expect(moovieListReviewApi.getMoovieListReviewsFromUserId).toHaveBeenCalledWith(1, 1);
        expect(result).toEqual({ data: mockReviews });
    });

    test('should edit a moovie list review', async () => {
        const mockResponse = { data: { id: 1, reviewContent: "Updated review" } };
        moovieListReviewApi.editReview.mockResolvedValue(mockResponse);

        const result = await MoovieListReviewService.editReview(1, "Updated review");

        expect(moovieListReviewApi.editReview).toHaveBeenCalledWith(1, "Updated review");
        expect(result).toEqual(mockResponse);
    });

    test('should create a moovie list review', async () => {
        const mockResponse = { data: { id: 1, reviewContent: "Amazing movie!" } };
        moovieListReviewApi.createMoovieListReview.mockResolvedValue(mockResponse);

        const result = await MoovieListReviewService.createMoovieListReview(1, "Amazing movie!");

        expect(moovieListReviewApi.createMoovieListReview).toHaveBeenCalledWith(1, "Amazing movie!");
        expect(result).toEqual(mockResponse);
    });

    test('should delete a moovie list review by ID', async () => {
        const mockResponse = { status: 200 }; // Assume successful deletion
        moovieListReviewApi.deleteMoovieListReviewById.mockResolvedValue(mockResponse);

        const result = await MoovieListReviewService.deleteMoovieListReview(1);

        expect(moovieListReviewApi.deleteMoovieListReviewById).toHaveBeenCalledWith(1);
        expect(result).toEqual(mockResponse);
    });

    test('should like a moovie list review', async () => {
        const mockResponse = { status: 200 }; // Assume successful like
        moovieListReviewApi.likeMoovieListReview.mockResolvedValue(mockResponse);

        const result = await MoovieListReviewService.likeMoovieListReview(1);

        expect(moovieListReviewApi.likeMoovieListReview).toHaveBeenCalledWith(1);
        expect(result).toEqual(mockResponse);
    });

});
