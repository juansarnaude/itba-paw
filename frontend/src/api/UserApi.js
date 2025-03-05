import api from './api.js';
import VndType from "../enums/VndType";

const userApi = (() => {

    // AUTHENTICATION STUFF

    const login = async ({ username, password }) => {
        const credentials = btoa(`${username}:${password}`);
        try {
            const response = await api.get(`/users/${username}`, {
                headers: {
                    'Authorization': `Basic ${credentials}`,
                }
            });
            const token = response.headers.get('Authorization');
            if (token) {
                sessionStorage.setItem('jwtToken', token);
                sessionStorage.setItem('username', username);
            } else {
                throw new Error(response.data.message);
            }
            return response;
        } catch (error) {
            throw error;
        }
    };

    const register = async ({ email, username, password }) => {
        try {
            return await api.post('/users', {
                    email,
                    username,
                    password,
                },
                {
                    headers: {
                        'Content-Type': VndType.APPLICATION_USER_FORM
                    }
                });
        } catch (error) {
            throw error;
        }
    };

    const confirmToken = async (token) => {
        const response = await api.put(`users/`,
            {
                token: token
            },
            {
                headers: {
                    'Content-Type': VndType.APPLICATION_USER_TOKEN_FORM
                }
            }
        );
        const jwtToken = response.headers['authorization'];
        if (jwtToken) {
            sessionStorage.setItem('jwtToken', jwtToken);
            localStorage.setItem('jwtToken', jwtToken);
        }
        return response;
    };

    const resendVerificationEmail = async (token) => {
        try {
            return await api.post('users/',
                { token },
                {
                    headers: {
                        'Content-Type': VndType.APPLICATION_RESEND_TOKEN_FORM
                    }
                }
            );
        } catch (error) {
            throw error;
        }
    };

    const forgotPassword = async (email) => {
        try {
            return await api.post('/users/',
                { email: email },
                {
                    headers: {
                        'Content-Type': VndType.APPLICATION_PASSWORD_TOKEN_FORM
                    }
                }
            );
        } catch (error) {
            throw error;
        }
    }

    const resetPassword = async (token, password) => {
        try {
            return await api.put(`users/`,
                {
                    password: password,
                    token: token
                },
                {
                    headers: {
                        'Content-Type': VndType.APPLICATION_USER_PASSWORD
                    }
                }
            );
        } catch (error) {
            throw error;
        }
    }

    const listUsers = ({role}) => {
        return api.get('/users', {params: {role}});
    };

    const getBanMessage = (username) => {
        try {
            return api.get(`/users/${username}/banMessage`);
        } catch (error) {
            return " ";
        }
    };

    const authTest = async () => {
        try {
            const response = await api.get('/users/authtest');
            return response.status === 200;
        } catch (error) {
            console.error('Auth test error:', error);
            return false;
        }
    };

    const getUsersCount = () => {
        return api.get('users/count');
    };

    // MODERATION STUFF

    const banUser = (username) => {
        const banUserDTO = {
            modAction: "BAN",
            banMessage: "User banned by moderator"
        };
        return api.put(`/users/${username}`, banUserDTO, {
            headers: {
                'Content-Type': VndType.APPLICATION_USER_BAN_FORM
            }
        });
    };

    const unbanUser = (username) => {
        const banUserDTO = {
            modAction: "UNBAN",
        };
        return api.put(`/users/${username}`, banUserDTO, {
            headers: {
                'Content-Type': VndType.APPLICATION_USER_BAN_FORM
            }
        });
    };

    const makeUserModerator = (username) => {
        return api.put(`/users/${username}`, {
        });
    };

    return {
        login,
        register,
        listUsers,
        authTest,
        getUsersCount,
        banUser,
        unbanUser,
        makeUserModerator,
        confirmToken,
        resendVerificationEmail,
        getBanMessage,
        forgotPassword,
        resetPassword
    };

})();

export default userApi;
