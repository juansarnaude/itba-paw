import ListService from "../../services/ListService";
import listApi from "../../api/ListApi";
import { parsePaginatedResponse } from "../../utils/ResponseUtils";
import MoovieListTypes from "../../api/values/MoovieListTypes";

// Mock the API module and utils
jest.mock("../../api/ListApi");
jest.mock("../../utils/ResponseUtils");

describe("ListService", () => {

    beforeEach(() => {
        jest.clearAllMocks(); // Clear previous test mocks
    });

    test("should fetch lists with pagination and filtering", async () => {
        const mockLists = {
            data: [
                { id: 1, name: "List 1" },
                { id: 2, name: "List 2" },
            ],
            links: null
        };

        listApi.getLists.mockResolvedValue({ data: mockLists.data });
        parsePaginatedResponse.mockReturnValue(mockLists);

        const searchParams = {
            search: "test",
            ownerUsername: "owner",
            type: "public",
            orderBy: "name",
            order: "asc",
            pageNumber: 1,
            pageSize: 10
        };

        const result = await ListService.getLists(searchParams);

        expect(listApi.getLists).toHaveBeenCalledWith(searchParams);
        expect(result).toEqual(mockLists);
    });

    test("should fetch list by id", async () => {
        const mockList = { id: 1, name: "My List" };
        listApi.getListById.mockResolvedValue({ data: mockList });

        const result = await ListService.getListById(1);

        expect(listApi.getListById).toHaveBeenCalledWith(1);
        expect(result).toEqual({ data: mockList });
    });

    test("should fetch list by list IDs", async () => {
        const idListString = "1,2,3";
        const mockLists = [
            { id: 1, name: "List 1" },
            { id: 2, name: "List 2" },
            { id: 3, name: "List 3" },
        ];
        listApi.getListByIdList.mockResolvedValue({ data: mockLists });

        const result = await ListService.getListByIdList(idListString);

        expect(listApi.getListByIdList).toHaveBeenCalledWith(idListString);
        expect(result).toEqual({ data: mockLists });
    });

    test("should create a new moovie list", async () => {
        const mockResponse = { id: 1, name: "New List", description: "Description" };
        listApi.createMoovieList.mockResolvedValue({ data: mockResponse });

        const result = await ListService.createMoovieList({ name: "New List", type:MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PUBLIC,  description: "Description" });

        expect(listApi.createMoovieList).toHaveBeenCalledWith("New List", MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PUBLIC, "Description");
        expect(result).toEqual({ data: mockResponse });
    });

    test("should edit a moovie list", async () => {
        const mockResponse = { id: 1, name: "Updated List", description: "Updated description" };
        listApi.editMoovieList.mockResolvedValue({ data: mockResponse });

        const result = await ListService.editMoovieList({ id: 1, name: "Updated List", description: "Updated description" });

        expect(listApi.editMoovieList).toHaveBeenCalledWith(1, "Updated List", "Updated description");
        expect(result).toEqual({ data: mockResponse });
    });

    test("should insert media into a moovie list", async () => {
        const mockResponse = { success: true };
        const mockMediaIds = [1, 2, 3];
        listApi.insertMediaIntoMoovieList.mockResolvedValue({ data: mockResponse });

        const result = await ListService.insertMediaIntoMoovieList({ id: 1, mediaIds: mockMediaIds });

        expect(listApi.insertMediaIntoMoovieList).toHaveBeenCalledWith({ id: 1, mediaIds: mockMediaIds });
        expect(result).toEqual({ data: mockResponse });
    });

    test("should delete media from a moovie list", async () => {
        const mockResponse = { success: true };
        listApi.deleteMediaFromMoovieList.mockResolvedValue({ data: mockResponse });

        const result = await ListService.deleteMediaFromMoovieList({ id: 1, mediaId: 2 });

        expect(listApi.deleteMediaFromMoovieList).toHaveBeenCalledWith({ id: 1, mediaId: 2 });
        expect(result).toEqual({ data: mockResponse });
    });

    test("should get recommended lists for a list ID", async () => {
        const mockRecommendedLists = [
            { id: 1, name: "Recommended List 1" },
            { id: 2, name: "Recommended List 2" }
        ];
        listApi.getRecommendedLists.mockResolvedValue({ data: mockRecommendedLists });

        const result = await ListService.getRecommendedLists(1);

        expect(listApi.getRecommendedLists).toHaveBeenCalledWith(1);
        expect(result).toEqual({ data: mockRecommendedLists });
    });

});
