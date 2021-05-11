package com.trevorism.gcloud.webapi.controller

import com.trevorism.data.Repository
import com.trevorism.gcloud.webapi.model.TrevorismList
import org.junit.Test

class TrevorismListControllerTest {

    @Test
    void testRead() {
        TrevorismListController filterController = new TrevorismListController()
        filterController.trevorismListRepository = [get: { id -> new TrevorismList(id: id, name: "testFilter")}] as Repository
        def readFilter = filterController.read(12345)
        assert readFilter.id == "12345"
        assert readFilter.name == "testFilter"

    }

    @Test
    void testReadAll() {
        TrevorismListController filterController = new TrevorismListController()
        filterController.trevorismListRepository = [list: {[new TrevorismList(id: 12345, name: "testFilter")]}] as Repository
        def listFilter = filterController.readAll()
        assert listFilter
        assert listFilter[0]
    }

    @Test
    void testCreate() {
        TrevorismListController filterController = new TrevorismListController()
        filterController.trevorismListRepository = [create: {new TrevorismList(id: 12345, name: "testFilter")}] as Repository

        TrevorismList filter = new TrevorismList(name: "first", description: "val", url: "=", selfHosted: true)
        assert filterController.create(filter)
    }

    @Test
    void testUpdate() {
        TrevorismListController filterController = new TrevorismListController()
        filterController.trevorismListRepository = [update: {id, filter -> new TrevorismList(id: 12345, name: "testFilter")}] as Repository

        assert filterController.update(6271336783544320, new TrevorismList(name: "first", description: "val", url: "=", selfHosted: true))
    }

    @Test
    void testDelete() {
        TrevorismListController filterController = new TrevorismListController()
        filterController.trevorismListRepository = [delete: { id -> new TrevorismList(id: id, name: "testFilter")}] as Repository
        assert filterController.delete(5164859054358528)
    }
}
