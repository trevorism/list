package com.trevorism.gcloud.webapi.filter

import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter
import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider

/**
 * @author tbrooks
 */
@Provider
@Created
class CreatedFilter implements ContainerResponseFilter {

    @Override
    void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        if (!responseContext.entity)
            return
        responseContext.setStatus(Response.Status.CREATED.statusCode)
    }

}