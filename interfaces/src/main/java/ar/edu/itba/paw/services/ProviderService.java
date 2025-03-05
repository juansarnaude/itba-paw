package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Provider.Provider;

import java.util.List;

public interface ProviderService {
    List<Provider> getAllProviders();
    List<Provider> getProvidersForMedia(final int mediaId);
    Provider getProviderById(final int id);

}