import MediaService from "../../services/MediaService";
import mediaApi from "../../api/MediaApi";
import { parsePaginatedResponse } from "../../utils/ResponseUtils";

jest.mock("../../api/MediaApi");
jest.mock("../../utils/ResponseUtils");

describe("MediaService", () => {

    beforeEach(() => {
        jest.clearAllMocks();
    });

    test("should fetch media with pagination and filtering", async () => {
        const mockMedia = {
            data: [
                { id: 1, title: "Movie 1" },
                { id: 2, title: "Movie 2" }
            ],
            links: null
        };

        mediaApi.getMedia.mockResolvedValue({ data: mockMedia.data });
        parsePaginatedResponse.mockReturnValue(mockMedia);

        const searchParams = {
            type: "movie",
            page: 1,
            pageSize: 10,
            orderBy: "name",
            sortOrder: "asc",
            search: "test",
            providers: "Netflix",
            genres: "Action"
        };

        const result = await MediaService.getMedia(searchParams);

        expect(mediaApi.getMedia).toHaveBeenCalledWith(searchParams);
        expect(result).toEqual(mockMedia);
    });

    test("should fetch media by ID list", async () => {
        const mockMediaList = [
            { id: 1, title: "Movie 1" },
            { id: 2, title: "Movie 2" }
        ];
        const idListString = "1,2";
        mediaApi.getMediaByIdList.mockResolvedValue({ data: mockMediaList });

        const result = await MediaService.getMediaByIdList(idListString);

        expect(mediaApi.getMediaByIdList).toHaveBeenCalledWith(idListString);
        expect(result).toEqual({ data: mockMediaList });
    });

    test("should fetch providers for media", async () => {
        const mockProviders = [
            { name: "Netflix" },
            { name: "Amazon Prime" }
        ];
        mediaApi.getProvidersForMedia.mockResolvedValue({ data: mockProviders });

        const result = await MediaService.getProvidersForMedia({ url: "/providers" });

        expect(mediaApi.getProvidersForMedia).toHaveBeenCalledWith('/providers');
        expect(result).toEqual({ data: mockProviders });
    });

    test("should fetch media for TV creator", async () => {
        const mockMedia = [
            { id: 1, title: "TV Show 1" },
            { id: 2, title: "TV Show 2" }
        ];
        mediaApi.getMediasForTVCreator.mockResolvedValue({ data: mockMedia });

        const result = await MediaService.getMediasForTVCreator(1);

        expect(mediaApi.getMediasForTVCreator).toHaveBeenCalledWith(1);
        expect(result).toEqual({ data: mockMedia });
    });

    test("should fetch media for director", async () => {
        const mockMedia = [
            { id: 1, title: "Director Movie 1" },
            { id: 2, title: "Director Movie 2" }
        ];
        mediaApi.getMediasForDirector.mockResolvedValue({ data: mockMedia });

        const result = await MediaService.getMediasForDirector(1);

        expect(mediaApi.getMediasForDirector).toHaveBeenCalledWith(1);
        expect(result).toEqual({ data: mockMedia });
    });

    test("should fetch media for actor", async () => {
        const mockMedia = [
            { id: 1, title: "Actor Movie 1" },
            { id: 2, title: "Actor Movie 2" }
        ];
        mediaApi.getMediasForActor.mockResolvedValue({ data: mockMedia });

        const result = await MediaService.getMediasForActor(1);

        expect(mediaApi.getMediasForActor).toHaveBeenCalledWith(1);
        expect(result).toEqual({ data: mockMedia });
    });

    test("should extract media IDs from object list", () => {
        const mockList = [
            { mediaId: 1, title: "Movie 1" },
            { mediaId: 2, title: "Movie 2" }
        ];

        const result = MediaService.getIdMediaFromObjectList(mockList);

        expect(result).toEqual("1,2");
    });
});
