import GenreService from "../../services/GenreService";
import genreApi from "../../api/GenreApi";

// Mock the API module
jest.mock("../../api/GenreApi");

describe("GenreService", () => {

    beforeEach(() => {
        jest.clearAllMocks(); // Clear previous test mocks
    });

    test("should fetch all genres", async () => {
        // Mock API response
        const mockGenres = [
            { id: 1, name: "Action" },
            { id: 2, name: "Comedy" },
        ];
        genreApi.getAllGenres.mockResolvedValue({ data: mockGenres });

        // Call service
        const result = await GenreService.getAllGenres();

        // Expectations
        expect(genreApi.getAllGenres).toHaveBeenCalled();
        expect(result.data).toEqual(mockGenres);
    });

    test("should fetch genres for media", async () => {
        const mediaId = 123;
        const mockGenresForMedia = [
            { id: 1, name: "Drama" },
            { id: 2, name: "Thriller" },
        ];
        genreApi.getGenresForMedia.mockResolvedValue({ data: mockGenresForMedia });

        const result = await GenreService.getGenresForMedia(mediaId);

        expect(genreApi.getGenresForMedia).toHaveBeenCalledWith(mediaId);
        expect(result.data).toEqual(mockGenresForMedia);
    });

});
