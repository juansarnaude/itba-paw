package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Provider.Provider;

import java.util.List;

public interface ProviderDao {
    List<Provider> getAllProviders();
    List<Provider> getProvidersForMedia(final int mediaId);
    Provider getProviderById(final int id);
}
