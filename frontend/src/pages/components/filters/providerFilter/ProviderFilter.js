import React from 'react';
import './providerFilter.css';

const ProviderFilter = ({ providers, selectedProviders, setSelectedProviders }) => {
    const handleProviderClick = (provider) => {
        const updatedProviders = new Set(selectedProviders);
        if (updatedProviders.has(provider.providerId)) {
            updatedProviders.delete(provider.providerId);
        } else {
            updatedProviders.add(provider.providerId);
        }
        setSelectedProviders(updatedProviders);
    };

    return (
        <div className="provider-filter">
            {providers && providers.map((provider) => (
                <div
                    key={provider.providerId}
                    className={`provider-item ${selectedProviders.has(provider.providerId) ? 'selected' : ''}`}
                    onClick={() => handleProviderClick(provider)}
                >
                    <img src={provider.logoPath} alt={provider.providerName} className="provider-logo" />
                    <span>{provider.providerName}</span>
                    {selectedProviders.has(provider.providerId) && (
                        <span className="selected-indicator">Selected</span>
                    )}
                </div>
            ))}
        </div>
    );
};

export default ProviderFilter;
