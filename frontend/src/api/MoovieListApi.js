import api from './api.js';

const moovieListApi = (() => {

    const deleteMoovieList = async (mlId) => {
        const response = await api.delete('/moovieList/' + mlId);
        return response;
    }

    return {
        deleteMoovieList
    }

})()

export default moovieListApi;