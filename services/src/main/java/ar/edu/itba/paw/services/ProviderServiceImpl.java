package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.ResourceNotFoundException;
import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Provider.Provider;
import ar.edu.itba.paw.persistence.ProviderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService{
    @Autowired
    private ProviderDao providerDao;

    @Transactional(readOnly = true)
    @Override
    public List<Provider> getAllProviders() {
        return providerDao.getAllProviders();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Provider> getProvidersForMedia(final int mediaId) {
        return providerDao.getProvidersForMedia(mediaId);
    }

    @Transactional(readOnly = true)
    @Override
    public Provider getProviderById (int providerId) {
        Provider provider = providerDao.getProviderById(providerId);
        if(provider == null){
            throw new ResourceNotFoundException("Provider with id " + providerId + " not found");
        }
        return provider;
    }
}
