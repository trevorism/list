package com.trevorism.gcloud.webapi.service

import com.trevorism.data.Repository
import com.trevorism.gcloud.webapi.EchoSecureHttpClient
import com.trevorism.gcloud.webapi.model.Content
import com.trevorism.gcloud.webapi.model.TrevorismList
import org.junit.jupiter.api.Test

class DefaultListContentServiceTest {

    @Test
    void testCreate() {
        DefaultListContentService defaultListContentService = new DefaultListContentService(new EchoSecureHttpClient())
        defaultListContentService.trevorismListRepository = [create: { new TrevorismList(selfHosted: true)}] as Repository
        defaultListContentService.listContentRepository = [create: {}] as Repository

        TrevorismList trevorismList = new TrevorismList(name: "firstList", description: "just a sample non-self hosted list", url: "https://datastore.data.trevorism.com/object", selfHosted: false, )
        TrevorismList trevorismList2 = new TrevorismList(name: "secondList", description: "just a sample self hosted list", url: "https://list.data.trevorism.com/api/", selfHosted: true )

        assert defaultListContentService.create(trevorismList)
        assert defaultListContentService.create(trevorismList2)
    }

    @Test
    void testRead() {
        DefaultListContentService defaultListContentService = new DefaultListContentService(new EchoSecureHttpClient())
        defaultListContentService.trevorismListRepository = [get: { new TrevorismList(selfHosted: true)}] as Repository
        assert defaultListContentService.read(5202267682635776)
    }

    @Test
    void testReadAll() {
        DefaultListContentService defaultListContentService = new DefaultListContentService(new EchoSecureHttpClient())
        defaultListContentService.trevorismListRepository = [list: { [new TrevorismList(selfHosted: true)]}] as Repository
        assert defaultListContentService.readAll()
    }

    @Test
    void testUpdate() {
        DefaultListContentService defaultListContentService = new DefaultListContentService(new EchoSecureHttpClient())
        defaultListContentService.trevorismListRepository = [update: { id, list -> list}] as Repository

        assert defaultListContentService.update(5202267682635776, new TrevorismList(description: "hi"))
    }

    @Test
    void testDelete() {
        DefaultListContentService defaultListContentService = new DefaultListContentService(new EchoSecureHttpClient())
        defaultListContentService.trevorismListRepository = [delete: { new TrevorismList(selfHosted: true)}] as Repository
        assert defaultListContentService.delete(5202267682635776)
    }

    @Test
    void testAddListContent() {
        DefaultListContentService defaultListContentService = new DefaultListContentService(new EchoSecureHttpClient())
        defaultListContentService.listContentRepository = [filter: {[new Content(trevorismListId: 5202267682635776, data: [])]}, update: { id, list -> list}] as Repository
        assert defaultListContentService.addListContent(5202267682635776, "self1")
        assert defaultListContentService.addListContent(5202267682635776, "self1").data.contains("self1")
    }

    @Test
    void testReplaceListContent() {
        DefaultListContentService defaultListContentService = new DefaultListContentService(new EchoSecureHttpClient())
        defaultListContentService.listContentRepository = [filter: {[new Content(trevorismListId: 5202267682635776, data: [])]}, update: { id, list -> list}] as Repository
        assert defaultListContentService.replaceListContent(5202267682635776, ["self1"])
        assert defaultListContentService.replaceListContent(5202267682635776, ["self1"]).data.contains("self1")
    }

    @Test
    void testRemoveListContent() {
        DefaultListContentService defaultListContentService = new DefaultListContentService(new EchoSecureHttpClient())
        defaultListContentService.listContentRepository = [filter: {[new Content(trevorismListId: 5202267682635776, data: [])]}, update: { id, list -> list}] as Repository
        assert defaultListContentService.removeListContent(5202267682635776, "self1")
        assert !defaultListContentService.removeListContent(5202267682635776, "self1").data.contains("self1")
    }

    @Test
    void testGetContent() {
        DefaultListContentService defaultListContentService = new DefaultListContentService(new EchoSecureHttpClient())
        defaultListContentService.listContentRepository = [filter:  {[new Content(trevorismListId: 5202267682635776, data: [])]}] as Repository
        assert defaultListContentService.getContent(5202267682635776)
    }
    
}
