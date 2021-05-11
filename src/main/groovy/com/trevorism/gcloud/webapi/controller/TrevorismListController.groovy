package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.filter.Created
import com.trevorism.gcloud.webapi.model.Content
import com.trevorism.gcloud.webapi.model.TrevorismList
import com.trevorism.gcloud.webapi.service.DefaultListContentService
import com.trevorism.gcloud.webapi.service.ListContentService
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.BadRequestException
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import java.util.logging.Logger

@Api("List Operations")
@Path("api")
class TrevorismListController {
    private static final Logger log = Logger.getLogger(TrevorismListController.class.name)
    private ListContentService service = new DefaultListContentService()

    @ApiOperation(value = "Get a list with id {id} **Secure")
    @GET
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TrevorismList read(@PathParam("id") long id){
        service.read(id)
    }

    @ApiOperation(value = "Get all lists")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<TrevorismList> readAll(){
        service.readAll()
    }

    @ApiOperation(value = "Create a list **Secure")
    @POST
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Created
    TrevorismList create(TrevorismList trevorismList){
        try {
            service.create(trevorismList)
        }catch (Exception e){
            log.severe("Unable to create List object: ${trevorismList} :: ${e.getMessage()}")
            throw new BadRequestException(e)
        }
    }

    @ApiOperation(value = "Update a list with id {id} **Secure")
    @PUT
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TrevorismList update(@PathParam("id") long id, TrevorismList trevorismList){
        service.update(id, trevorismList)
    }

    @ApiOperation(value = "Delete a list with id {id} **Secure")
    @DELETE
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TrevorismList delete(@PathParam("id") long id){
        service.delete(id)
    }

    @ApiOperation(value = "Get the list contents with id {id} **Secure")
    @GET
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Path("{id}/content")
    @Produces(MediaType.APPLICATION_JSON)
    Content getContents(@PathParam("id") long id){
        service.getContent(id)
    }

    @ApiOperation(value = "Get the list contents with id {id} **Secure")
    @POST
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Path("{id}/content")
    @Produces(MediaType.APPLICATION_JSON)
    Content addContent(@PathParam("id") long id, String item){
        service.addListContent(id, item)
    }

    @ApiOperation(value = "Replace the list contents with id {id} **Secure")
    @PUT
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Path("{id}/content")
    @Produces(MediaType.APPLICATION_JSON)
    Content replaceContent(@PathParam("id") long id, List<String> items){
        service.replaceListContent(id, items)
    }

    @ApiOperation(value = "Delete an item from the list contents with id {id} **Secure")
    @DELETE
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Path("{id}/content/{content}")
    @Produces(MediaType.APPLICATION_JSON)
    Content deleteContent(@PathParam("id") long id, @PathParam("content") String item){
        service.removeListContent(id, item)
    }
}