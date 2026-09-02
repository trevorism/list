package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.Content
import com.trevorism.gcloud.webapi.model.TrevorismList
import com.trevorism.gcloud.webapi.service.ListContentService
import com.trevorism.secure.Secure
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import org.junit.jupiter.api.Test

import java.lang.reflect.Method

class ListControllerTest {

    @Test
    void testDeleteRemovesTheList() {
        ListController controller = new ListController(service:
                [delete: { new TrevorismList(id: "5", name: "removed") }] as ListContentService)

        assert controller.delete(5).name == "removed"
    }

    @Test
    void testGetContentsFallsBackToNonSelfHostedData() {
        ListController controller = new ListController(service:
                [getContent          : { null },
                 getNonSelfHostedData: { new Content(trevorismListId: "5", data: ["remote"]) }] as ListContentService)

        assert controller.getContents(5).data == ["remote"]
    }

    @Test
    void testGetRandomContentSelectsFromEveryStoredItem() {
        ListController controller = new ListController(service:
                [getContent: { new Content(trevorismListId: "5", data: ["one", "two", "three"]) }] as ListContentService)

        Set<String> selections = (1..200).collect { controller.getRandomContent(5) } as Set

        assert selections == ["one", "two", "three"] as Set
    }

    @Test
    void testEveryMutatingRouteDeclaresSecure() {
        List<String> unsecured = ListController.declaredMethods.findAll { Method method ->
            [Post, Put, Delete].any { method.isAnnotationPresent(it) } && !method.isAnnotationPresent(Secure)
        }.collect { it.name }.sort()

        assert unsecured == []
    }
}
