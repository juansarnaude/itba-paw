package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Provider.Provider;
import ar.edu.itba.paw.services.ProviderService;
import ar.edu.itba.paw.webapp.dto.out.GenreDto;
import ar.edu.itba.paw.webapp.dto.out.ProviderDto;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("providers")
@Component
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(VndType.APPLICATION_PROVIDER_LIST)
    public Response getProviders(@QueryParam("mediaId") final Integer mediaId) {
        try {
            List<Provider> providerList;

            // Si se proporciona un mediaId, obtener proveedores para ese medio
            if (mediaId != null) {
                providerList = providerService.getProvidersForMedia(mediaId);
            }
            // Si no se proporciona un mediaId, obtener todos los proveedores
            else {
                providerList = providerService.getAllProviders();
            }

            // Convertir la lista de proveedores a DTOs
            final List<ProviderDto> providerDtoList = ProviderDto.fromProviderList(providerList, uriInfo);

            // Devolver la respuesta con la lista de DTOs
            return Response.ok(new GenericEntity<List<ProviderDto>>(providerDtoList) {
            }).build();
        } catch (RuntimeException e) {
            // Manejar errores
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ResponseMessage(e.getMessage()))
                    .build();
        }
    }

    @GET()
    @Path("/{providerId}")
    @Produces(VndType.APPLICATION_PROVIDER)
    public Response getGenre(@PathParam("providerId") final Integer providerId) {
        Provider genre = providerService.getProviderById(providerId);
        return Response.ok(ProviderDto.fromProvider(genre, uriInfo)).build();
    }
}
