package com.trevorism.gcloud

import com.google.gson.Gson
import com.trevorism.gcloud.webapi.model.Content
import com.trevorism.gcloud.webapi.model.TrevorismList
import com.trevorism.https.AppClientSecureHttpClient
import com.trevorism.https.SecureHttpClient
import io.cucumber.groovy.EN
import io.cucumber.groovy.Hooks

/**
 * @author tbrooks
 */

this.metaClass.mixin(Hooks)
this.metaClass.mixin(EN)

SecureHttpClient secureHttpClient = new AppClientSecureHttpClient()
TrevorismList created
Gson gson = new Gson()
Content responseContent
Content updatedContent

Given(/a new list container is created/) {  ->
    String json = gson.toJson(new TrevorismList(name: "testList", description: "testDescription", selfHosted: true))
    String responseJson = secureHttpClient.post("https://list.data.trevorism.com/api", json)
    created = gson.fromJson(responseJson, TrevorismList.class)
}

Given(/contains content with the value {string}/) { String string ->
    secureHttpClient.post("https://list.data.trevorism.com/api/${created.id}/content", string)
}

When(/the container content is requested/) {  ->
    assert secureHttpClient.get("https://list.data.trevorism.com/api/${created.id}")
    String responseJson = secureHttpClient.get("https://list.data.trevorism.com/api/${created.id}/content")
    responseContent = gson.fromJson(responseJson, Content.class)
}

Then(/the content includes {string}/) { String string ->
    assert responseContent.data.contains(string)
}

Then(/the list container is deleted successfully/) {  ->
    assert secureHttpClient.delete("https://list.data.trevorism.com/api/${created.id}")
}

When(/the container content is updated with {string}/) { String string ->
    String json = gson.toJson(["test", string])
    String responseJson = secureHttpClient.put("https://list.data.trevorism.com/api/${created.id}/content", json)
    assert responseJson.contains(string)
    updatedContent = gson.fromJson(responseJson, Content.class)
}


Then(/the content includes {string} and {string}/) { String string, String string2 ->
    assert updatedContent.data.contains(string)
    assert updatedContent.data.contains(string2)
}