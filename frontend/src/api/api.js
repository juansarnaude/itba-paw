import axios from "axios";
import Qs from "qs";

const api = axios.create({
    baseURL: process.env.REACT_APP_API_URL,
    timeout: 50000,
    paramsSerializer: params => Qs.stringify(params, {arrayFormat: 'repeat'})
});

// Interceptor to add the token to all requests
api.interceptors.request.use(
    config => {
        const token = sessionStorage.getItem('jwtToken') || localStorage.getItem('jwtToken');
        if (token === undefined || token === null || token === 'undefined') {
            return config;
        }
        if (token) {
            config.headers['Authorization'] = `${token}`;
        }
        return config;
    }
);

api.interceptors.response.use(
    response => {
        return response;
    }
);

export default api;
