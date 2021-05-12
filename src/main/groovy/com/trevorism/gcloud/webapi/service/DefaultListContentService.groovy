package com.trevorism.gcloud.webapi.service

import com.google.gson.Gson
import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.gcloud.webapi.model.Content
import com.trevorism.gcloud.webapi.model.TrevorismList
import com.trevorism.https.DefaultSecureHttpClient
import com.trevorism.https.SecureHttpClient

import java.util.logging.Logger

class DefaultListContentService implements ListContentService {
    private static final Logger log = Logger.getLogger(DefaultListContentService.class.name)
    private Repository<TrevorismList> trevorismListRepository = new PingingDatastoreRepository<>(TrevorismList)
    private Repository<Content> listContentRepository = new PingingDatastoreRepository<>(Content)
    private Gson gson = new Gson()
    private SecureHttpClient httpClient = new DefaultSecureHttpClient()

    @Override
    TrevorismList create(TrevorismList list) {
        TrevorismList created = trevorismListRepository.create(list)
        if (created.selfHosted) {
            Content content = new Content(trevorismListId: created.id, data: [])
            listContentRepository.create(content)
        }
        return created
    }

    @Override
    TrevorismList read(long id) {
        trevorismListRepository.get(String.valueOf(id))
    }

    @Override
    List<TrevorismList> readAll() {
        trevorismListRepository.list()
    }

    @Override
    TrevorismList update(long id, TrevorismList list) {
        trevorismListRepository.update(String.valueOf(id), list)
    }

    @Override
    TrevorismList delete(long id) {
        trevorismListRepository.delete(String.valueOf(id))
    }

    @Override
    Content addListContent(long id, String item) {
        Content content = getContent(id)
        if (content) {
            content.data?.add(item)
            return listContentRepository.update(content.id, content)
        }
        return null
    }

    @Override
    Content replaceListContent(long id, List<String> items) {
        Content content = getContent(id)
        if (content) {
            content.data = items
            return listContentRepository.update(content.id, content)
        }
    }

    @Override
    Content removeListContent(long id, String item) {
        Content content = getContent(id)
        if (content) {
            content.data?.remove(item)
            return listContentRepository.update(content.id, content)
        }
    }

    @Override
    Content getContent(long id) {
        def allContents = listContentRepository.list()
        Content content = allContents.find {
            it.trevorismListId == String.valueOf(id)
        }
        return content
    }

    @Override
    Content getNonSelfHostedData(long id) {
        TrevorismList trevorismList = read(id)
        if (trevorismList.selfHosted) {
            return null
        }
        try {
            String json = httpClient.get(trevorismList.url)
            return new Content(trevorismListId: id, data: gson.fromJson(json, List).collect { it.toString() })
        } catch (Exception e) {
            log.warning("Unable to retrieve content from url: ${trevorismList.url} with error message: ${e.getMessage()}")
        }
        return null
    }
}
