package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.PagingUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class ResponseUtils {

    public ResponseUtils() {
        throw new AssertionError();
    }

    public static <T> void setPaginationLinks(Response.ResponseBuilder res, PagingUtils<T> pagingUtils, UriInfo uriInfo){
        if(pagingUtils.hasPreviousPage()){
            res.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page",pagingUtils.getCurrentPage() - 1).build().toString(),"previous");
        }
        if(pagingUtils.hasNextPage()){
            res.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page",pagingUtils.getCurrentPage() + 1).build().toString(),"next");
        }
        res.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page",pagingUtils.getFirstPage()).build().toString(),"first");
        res.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page",pagingUtils.getLastPage()).build().toString(),"last");
        res.header("Total-Elements",pagingUtils.getTotalCount());
    }
}
