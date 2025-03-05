package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Provider.Provider;

import javax.persistence.Column;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

public class ProviderDto {

    private int providerId;
    private String providerName;
    private String logoPath;
    private String url;

    public static ProviderDto fromProvider(Provider p, UriInfo uriInfo) {
        ProviderDto dto = new ProviderDto();
        dto.providerId = p.getProviderId();
        dto.providerName = p.getProviderName();
        dto.logoPath = p.getLogoPath();
        dto.url = uriInfo.getBaseUriBuilder().path("providers/{providerId}").build(p.getProviderId()).toString();
        return dto;
    }

    public static List<ProviderDto> fromProviderList(List<Provider> providerList, UriInfo uriInfo){
        return providerList.stream().map(m -> fromProvider(m,uriInfo)).collect(Collectors.toList());
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
