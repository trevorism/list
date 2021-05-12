package com.trevorism.gcloud.webapi.service

import com.trevorism.gcloud.webapi.model.Content
import com.trevorism.gcloud.webapi.model.TrevorismList

interface ListContentService {

    TrevorismList create(TrevorismList list)

    TrevorismList read(long id)

    List<TrevorismList> readAll()

    TrevorismList update(long id, TrevorismList list)

    TrevorismList delete(long id)

    Content addListContent(long id, String item)

    Content replaceListContent(long id, List<String> items)

    Content removeListContent(long id, String item)

    Content getContent(long id)

    Content getNonSelfHostedData(long id)
}