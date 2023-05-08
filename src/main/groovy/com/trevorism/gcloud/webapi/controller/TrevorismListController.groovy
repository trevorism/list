package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.Content
import com.trevorism.gcloud.webapi.model.TrevorismList
import com.trevorism.gcloud.webapi.service.DefaultListContentService
import com.trevorism.gcloud.webapi.service.ListContentService
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller("/api")
class TrevorismListController {
    private static final Logger log = LoggerFactory.getLogger(TrevorismListController.class.name)
    private ListContentService service = new DefaultListContentService()

    @Tag(name = "List Operations")
    @Operation(summary = "Get a list with id {id} **Secure")
    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    TrevorismList read(long id){
        service.read(id)
    }

    @Tag(name = "List Operations")
    @Operation(summary = "Get all lists")
    @Get(value = "/", produces = MediaType.APPLICATION_JSON)
    List<TrevorismList> readAll(){
        service.readAll()
    }

    @Tag(name = "List Operations")
    @Operation(summary = "Create a list **Secure")
    @Post(value = "/", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    TrevorismList create(@Body TrevorismList trevorismList){
        try {
            service.create(trevorismList)
        }catch (Exception e){
            log.severe("Unable to create List object: ${trevorismList} :: ${e.getMessage()}")
            throw new RuntimeException(e)
        }
    }

    @Tag(name = "List Operations")
    @Operation(summary = "Update a list with id {id} **Secure")
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    @Put(value = "/{id}", produces = MediaType.APPLICATION_JSON,  consumes = MediaType.APPLICATION_JSON)
    TrevorismList update(long id, @Body TrevorismList trevorismList){
        service.update(id, trevorismList)
    }

    @Tag(name = "List Operations")
    @Operation(summary = "Delete a list with id {id} **Secure")
    @Delete(value = "{id}", produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    TrevorismList delete(long id){
        service.delete(id)
    }

    @Tag(name = "List Operations")
    @Operation(summary = "Get the list contents with id {id} **Secure")
    @Get(value = "/{id}/content", produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    Content getContents(long id){
        def content = service.getContent(id)
        if(!content){
            content = service.getNonSelfHostedData(id)
        }
        return content
    }

    @Tag(name = "List Operations")
    @Operation(summary = "Get the list contents with id {id} **Secure")
    @Post(value = "/{id}/content", produces = MediaType.APPLICATION_JSON,  consumes = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    Content addContent(long id, @Body String item){
        service.addListContent(id, item)
    }

    @Tag(name = "List Operations")
    @Operation(summary = "Replace the list contents with id {id} **Secure")
    @Put(value = "/{id}/content", produces = MediaType.APPLICATION_JSON,  consumes = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    Content replaceContent(long id, @Body List<String> items){
        service.replaceListContent(id, items)
    }

    @Tag(name = "List Operations")
    @Operation(summary = "Delete an item from the list contents with id {id} **Secure")
    @Delete(value = "/{id}/content/{content}", produces = MediaType.APPLICATION_JSON,  consumes = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.SYSTEM, allowInternal = true)
    Content deleteContent(long id, String content){
        service.removeListContent(id, content)
    }
}