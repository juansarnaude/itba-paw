import ProfileService from '../../services/ProfileService';
import profileApi from '../../api/ProfileApi';
import { parsePaginatedResponse } from '../../utils/ResponseUtils';


jest.mock('../../api/ProfileApi'); // Mock the entire ProfileApi module

describe('ProfileService', () => {
    afterEach(() => {
        jest.clearAllMocks(); // Clear mocks after each test
    });

    beforeEach(() => {
        profileApi.getMilkyLeaderboard = jest.fn();
        profileApi.getSearchedUsers = jest.fn();
        profileApi.setPfp = jest.fn();
        profileApi.getSpecialListFromUser = jest.fn();
        profileApi.getLikedOrFollowedListFromUser = jest.fn();
        profileApi.currentUserHasLikedList = jest.fn();
        profileApi.likeList = jest.fn();
        profileApi.unlikeList = jest.fn();
        profileApi.currentUserHasFollowedList = jest.fn();
        profileApi.followList = jest.fn();
        profileApi.unfollowList = jest.fn();
        profileApi.currentUserWW = jest.fn();
        profileApi.insertMediaIntoWW = jest.fn();
        profileApi.removeMediaFromWW = jest.fn();
    });

    test('should fetch milky leaderboard', async () => {
        const mockLeaderboard = { data: [{ username: 'user1' }, { username: 'user2' }] };
        profileApi.getMilkyLeaderboard.mockResolvedValue(mockLeaderboard);

        const result = await ProfileService.getMilkyLeaderboard({ page: 1, pageSize: 10 });

        expect(profileApi.getMilkyLeaderboard).toHaveBeenCalledWith({ page: 1, pageSize: 10 });
        expect(result.data).toEqual(mockLeaderboard.data);
    });

    test('should fetch searched users', async () => {
        const mockUsers = { data: [{ username: 'user1' }, { username: 'user2' }] , links: null };
        profileApi.getSearchedUsers.mockResolvedValue(mockUsers);

        const result = await ProfileService.getSearchedUsers({
            username: 'user',
            orderBy: 'username',
            sortOrder: 'asc',
            page: 1
        });

        expect(profileApi.getSearchedUsers).toHaveBeenCalledWith({
            username: 'user',
            orderBy: 'username',
            sortOrder: 'asc',
            page: 1
        });
        expect(result).toEqual(mockUsers);
    });

    test('should set profile picture', async () => {
        const mockResponse = { status: 200 };
        profileApi.setPfp.mockResolvedValue(mockResponse);

        const result = await ProfileService.setPfp({ username: 'user1', pfp: 'image-data' });

        expect(profileApi.setPfp).toHaveBeenCalledWith('user1', 'image-data');
        expect(result).toEqual(mockResponse);
    });

    test('should fetch special list from user', async () => {
        const mockList = { data: [{ id: 1, name: 'Favorite Movies' }] , links: null };
        profileApi.getSpecialListFromUser.mockResolvedValue(mockList);

        const result = await ProfileService.getSpecialListFromUser({
            username: 'user1',
            type: 'watched',
            orderBy: 'name',
            order: 'asc',
            pageNumber: 1
        });

        expect(profileApi.getSpecialListFromUser).toHaveBeenCalledWith(
            'user1', 'watched', 'name', 'asc', 1
        );
        expect(result).toEqual(mockList);
    });

    test('should get liked or followed list from user', async () => {
        const mockList = { data: [{ id: 1, name: 'Liked List' }] , links: null };
        profileApi.getLikedOrFollowedListFromUser.mockResolvedValue(mockList);

        const result = await ProfileService.getLikedOrFollowedListFromUser(
            'user1', 'liked', 'name', 'asc', 1
        );

        expect(profileApi.getLikedOrFollowedListFromUser).toHaveBeenCalledWith(
            'user1', 'liked', 'name', 'asc', 1
        );
        expect(result).toEqual(mockList);
    });

    test('should check if user has liked a list', async () => {
        const mockResponse = { status: 200, data: { liked: true } };
        profileApi.currentUserHasLikedList.mockResolvedValue(mockResponse);

        const result = await ProfileService.currentUserHasLiked(1, 'user1');

        expect(profileApi.currentUserHasLikedList).toHaveBeenCalledWith(1, 'user1');
        expect(result).toEqual(true);
    });


    test('should check if user has followed a list', async () => {
        const mockResponse = { status: 200, data: { followed: true } };
        profileApi.currentUserHasFollowedList.mockResolvedValue(mockResponse);

        const result = await ProfileService.currentUserHasFollowed(1, 'user1');

        expect(profileApi.currentUserHasFollowedList).toHaveBeenCalledWith(1, 'user1');
        expect(result).toEqual(true);
    });

    test('should check user WW status', async () => {
        const mockStatus = { watched: true };
        profileApi.currentUserWW.mockResolvedValue(mockStatus);

        const result = await ProfileService.userWWStatus('watched',1, 'user1');

        expect(profileApi.currentUserWW).toHaveBeenCalledWith('watched', 'user1', 1);
        expect(result).toEqual(mockStatus.watched);
    });

    test('should insert media into WW', async () => {
        const mockResponse = { status: 200 };
        profileApi.insertMediaIntoWW.mockResolvedValue(mockResponse);

        const result = await ProfileService.insertMediaIntoWW('watched', 1, 'user1');

        expect(profileApi.insertMediaIntoWW).toHaveBeenCalledWith('watched', 1, 'user1');
        expect(result).toEqual(mockResponse);
    });

    test('should remove media from WW', async () => {
        const mockResponse = { status: 200 };
        profileApi.removeMediaFromWW.mockResolvedValue(mockResponse);

        const result = await ProfileService.removeMediaFromWW('watched', 1, 'user1');

        expect(profileApi.removeMediaFromWW).toHaveBeenCalledWith('watched', 'user1', 1);
        expect(result).toEqual(mockResponse);
    });
});
