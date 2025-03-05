import castApi from "../../api/CastApi";
import castService from "../../services/CastService";

jest.mock("../../api/CastApi"); // Mock the entire module

describe("castService", () => {

    beforeEach(() => {
        jest.clearAllMocks();  // Clear previous test mocks
    });

    test("should fetch actors for a query", async () => {
        const mockActors = [{ id: 1, name: "Actor One" }, { id: 2, name: "Actor Two" }];

        // Mock the response for `getActorsForQuery`
        castApi.getActorsForQuery.mockResolvedValue(mockActors);

        const result = await castService.getActorsForQuery("test");

        expect(castApi.getActorsForQuery).toHaveBeenCalledWith("test");
        expect(result).toEqual(mockActors);
    });

    test("should fetch actors by media ID and parse response", async () => {
        const mockResponse = { data: [{ id: 1, name: "Actor A" }], links: null };
        castApi.getActorsByMediaId.mockResolvedValue(mockResponse);

        const result = await castService.getActorsByMediaId(123);

        expect(castApi.getActorsByMediaId).toHaveBeenCalledWith(123);
        expect(result).toEqual(mockResponse);
    });

    test("should fetch directors for a query", async () => {
        const mockDirectors = [{ id: 10, name: "Director X" }];

        // Mock the response for `getDirectorsForQuery`
        castApi.getDirectorsForQuery.mockResolvedValue(mockDirectors);

        const result = await castService.getDirectorsForQuery("spielberg");

        expect(castApi.getDirectorsForQuery).toHaveBeenCalledWith("spielberg");
        expect(result).toEqual(mockDirectors);
    });

    test("should fetch TV creator by ID", async () => {
        const mockCreator = { id: 20, name: "TV Creator Y" };

        // Mock the response for `getTvCreatorById`
        castApi.getTvCreatorById.mockResolvedValue(mockCreator);

        const result = await castService.getTvCreatorById(20);

        expect(castApi.getTvCreatorById).toHaveBeenCalledWith(20);
        expect(result).toEqual(mockCreator);
    });

    test("should fetch TV creators by media ID and parse response", async () => {
        const mockResponse = { data: [{ id: 30, name: "Creator Z" }], links: null };
        castApi.getTvCreatorsByMediaId.mockResolvedValue(mockResponse);

        const result = await castService.getTvCreatorsByMediaId(200);

        expect(castApi.getTvCreatorsByMediaId).toHaveBeenCalledWith(200);
        expect(result).toEqual(mockResponse);
    });

});
