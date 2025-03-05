import userApi from '../../api/userApi';
import api from '../../api/api.js';
import VndType from "../../enums/VndType";

jest.mock('../../api/api.js');

describe('userApi', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test('login should authenticate and store token', async () => {
        const username = 'testUser';
        const password = 'testPass';
        const mockToken = 'Bearer mockJwtToken';
        const mockResponse = {
            headers: {
                get: jest.fn().mockReturnValue(mockToken),
            },
            data: {},
        };

        api.get.mockResolvedValue(mockResponse);

        Storage.prototype.setItem = jest.fn();

        await userApi.login({ username, password });

        expect(api.get).toHaveBeenCalledWith(`/users/${username}`, {
            headers: {
                'Authorization': `Basic ${btoa(`${username}:${password}`)}`,
            },
        });
        expect(sessionStorage.setItem).toHaveBeenCalledWith('jwtToken', mockToken);
        expect(sessionStorage.setItem).toHaveBeenCalledWith('username', username);
    });

    test('login should throw an error if no token is received', async () => {
        const username = 'testUser';
        const password = 'testPass';
        const mockResponse = {
            headers: {
                get: jest.fn().mockReturnValue(null),
            },
            data: { message: 'Invalid credentials' },
        };

        api.get.mockResolvedValue(mockResponse);

        await expect(userApi.login({ username, password })).rejects.toThrow('Invalid credentials');
    });

    test('register should send user details and return response', async () => {
        const userData = {
            email: 'test@example.com',
            username: 'testUser',
            password: 'testPass',
        };
        const mockResponse = { data: { success: true } };

        api.post.mockResolvedValue(mockResponse);

        const response = await userApi.register(userData);

        expect(api.post).toHaveBeenCalledWith('/users', userData, {
            headers: {
                'Content-Type': VndType.APPLICATION_USER_FORM,
            },
        });
        expect(response).toEqual(mockResponse);
    });

    test('confirmToken should send a token and store jwtToken if received', async () => {
        const token = 'mockToken';
        const mockResponse = {
            headers: { authorization: 'Bearer mockJwtToken' },
            data: {},
        };

        api.post.mockResolvedValue(mockResponse);

        Storage.prototype.setItem = jest.fn();

        const response = await userApi.confirmToken(token);

        expect(api.post).toHaveBeenCalledWith(`users/`, { token }, {
            headers: {
                'Content-Type': VndType.APPLICATION_USER_TOKEN_FORM,
            },
        });
        expect(sessionStorage.setItem).toHaveBeenCalledWith('jwtToken', 'Bearer mockJwtToken');
        expect(localStorage.setItem).toHaveBeenCalledWith('jwtToken', 'Bearer mockJwtToken');
        expect(response).toEqual(mockResponse);
    });

    test('authTest should return true if response is 200', async () => {
        const mockResponse = { status: 200 };

        api.get.mockResolvedValue(mockResponse);

        const result = await userApi.authTest();

        expect(api.get).toHaveBeenCalledWith('/users/authtest');
        expect(result).toBe(true);
    });

    test('authTest should return true if response is 200', async () => {
        const mockResponse = { status: 200 };

        api.get.mockResolvedValue(mockResponse); // Mock API response

        const result = await userApi.authTest();

        expect(api.get).toHaveBeenCalledWith('/users/authtest');
        expect(result).toBe(true);
    });

    test('authTest should return false if API call fails', async () => {
        api.get.mockRejectedValue(new Error('Network Error')); // Simulate a network error

        const result = await userApi.authTest();

        expect(api.get).toHaveBeenCalledWith('/users/authtest');
        expect(result).toBe(false);
    });

    test('banUser should send a ban request', async () => {
        const username = 'bannedUser';
        const mockResponse = { data: { success: true } };

        api.put.mockResolvedValue(mockResponse);

        const response = await userApi.banUser(username);

        expect(api.put).toHaveBeenCalledWith(`/users/${username}`, {
            modAction: "BAN",
            banMessage: "User banned by moderator",
        }, {
            headers: {
                'Content-Type': VndType.APPLICATION_USER_BAN_FORM,
            },
        });

        expect(response).toEqual(mockResponse);
    });

    test('unbanUser should send an unban request', async () => {
        const username = 'unbannedUser';
        const mockResponse = { data: { success: true } };

        api.put.mockResolvedValue(mockResponse);

        const response = await userApi.unbanUser(username);

        expect(api.put).toHaveBeenCalledWith(`/users/${username}`, {
            modAction: "UNBAN",
        }, {
            headers: {
                'Content-Type': VndType.APPLICATION_USER_BAN_FORM,
            },
        });

        expect(response).toEqual(mockResponse);
    });

    test('getUsersCount should fetch user count', async () => {
        const mockResponse = { data: { count: 10 } };

        api.get.mockResolvedValue(mockResponse);

        const response = await userApi.getUsersCount();

        expect(api.get).toHaveBeenCalledWith('users/count');
        expect(response).toEqual(mockResponse);
    });
});
