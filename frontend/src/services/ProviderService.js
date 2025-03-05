import providerApi from "../api/ProviderApi";

const ProviderService = (() => {

    const getAllProviders = async () => {
        return await providerApi.getAllProviders();
    }

    const getProvidersForMedia = async (id) => {
        return await providerApi.getProvidersForMedia(id);
    }

    return{
        getAllProviders,
        getProvidersForMedia
    }
})();

export default ProviderService;