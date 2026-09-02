package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.Content
import com.trevorism.gcloud.webapi.service.ListContentService
import com.trevorism.secure.Secure
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import org.junit.jupiter.api.Test

import java.lang.reflect.Method

class TrevorismListControllerTest {

    @Test
    void testGetContentsFromStoredContent() {
        TrevorismListController controller = new TrevorismListController(service:
                [getContent: { new Content(trevorismListId: "5", data: ["one"]) }] as ListContentService)

        assert controller.getContents(5).data == ["one"]
    }

    @Test
    void testGetContentsFallsBackToNonSelfHostedData() {
        TrevorismListController controller = new TrevorismListController(service:
                [getContent          : { null },
                 getNonSelfHostedData: { new Content(trevorismListId: "5", data: ["remote"]) }] as ListContentService)

        assert controller.getContents(5).data == ["remote"]
    }

    @Test
    void testGetRandomContentFromStoredContent() {
        TrevorismListController controller = new TrevorismListController(service:
                [getContent: { new Content(trevorismListId: "5", data: ["one", "two", "three"]) }] as ListContentService)

        Set<String> selections = (1..200).collect { controller.getRandomContent(5) } as Set

        assert selections == ["one", "two", "three"] as Set
    }

    @Test
    void testGetRandomContentFallsBackToNonSelfHostedData() {
        TrevorismListController controller = new TrevorismListController(service:
                [getContent          : { null },
                 getNonSelfHostedData: { new Content(trevorismListId: "5", data: ["remote"]) }] as ListContentService)

        assert controller.getRandomContent(5) == "remote"
    }

    @Test
    void testGetRandomContentWithoutContent() {
        TrevorismListController controller = new TrevorismListController(service:
                [getContent: { null }, getNonSelfHostedData: { null }] as ListContentService)

        assert !controller.getRandomContent(5)
    }

    @Test
    void testGetRandomContentWithEmptyContent() {
        TrevorismListController controller = new TrevorismListController(service:
                [getContent: { new Content(trevorismListId: "5", data: []) }] as ListContentService)

        assert !controller.getRandomContent(5)
    }

    @Test
    void testEveryMutatingRouteDeclaresSecure() {
        List<String> unsecured = TrevorismListController.declaredMethods.findAll { Method method ->
            [Post, Put, Delete].any { method.isAnnotationPresent(it) } && !method.isAnnotationPresent(Secure)
        }.collect { it.name }.sort()

        assert unsecured == []
    }
}
