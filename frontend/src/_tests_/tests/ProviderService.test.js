import ProviderService from '../../services/ProviderService';
import providerApi from '../../api/ProviderApi';

jest.mock('../../api/ProviderApi');

describe('ProviderService', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });

    test('should fetch all providers', async () => {
        const mockProviders = { data: [{ name: 'Provider 1' }, { name: 'Provider 2' }] };
        providerApi.getAllProviders.mockResolvedValue(mockProviders);

        const result = await ProviderService.getAllProviders();

        expect(providerApi.getAllProviders).toHaveBeenCalledTimes(1);
        expect(result).toEqual(mockProviders);
    });

    test('should fetch providers for media by ID', async () => {
        const mockProvidersForMedia = { data: [{ name: 'Provider 1', mediaId: 123 }] };
        providerApi.getProvidersForMedia.mockResolvedValue(mockProvidersForMedia);

        const mediaId = 123;
        const result = await ProviderService.getProvidersForMedia(mediaId);

        expect(providerApi.getProvidersForMedia).toHaveBeenCalledWith(mediaId);
        expect(result).toEqual(mockProvidersForMedia);
    });
});
