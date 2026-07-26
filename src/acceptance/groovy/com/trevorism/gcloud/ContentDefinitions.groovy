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

String baseUrl = System.getenv("ACCEPTANCE_BASE_URL") ?: "https://list.data.trevorism.com"
SecureHttpClient secureHttpClient = new AppClientSecureHttpClient()
TrevorismList created
Gson gson = new Gson()
Content responseContent
Content updatedContent
String randomItem

Given(/a new list container is created/) {  ->
    String json = gson.toJson(new TrevorismList(name: "testList", description: "testDescription", selfHosted: true))
    String responseJson = secureHttpClient.post("${baseUrl}/api", json)
    created = gson.fromJson(responseJson, TrevorismList.class)
}

Given(/contains content with the value {string}/) { String string ->
    secureHttpClient.post("${baseUrl}/api/${created.id}/content", string)
}

When(/the container content is requested/) {  ->
    assert secureHttpClient.get("${baseUrl}/api/${created.id}")
    String responseJson = secureHttpClient.get("${baseUrl}/api/${created.id}/content")
    responseContent = gson.fromJson(responseJson, Content.class)
}

When(/a random item from the container is requested/) {  ->
    randomItem = secureHttpClient.get("${baseUrl}/api/${created.id}/random")
}

Then(/the random item is {string}/) { String string ->
    assert randomItem.trim() == string
}

Then(/the content includes {string}/) { String string ->
    assert responseContent.data.contains(string)
}

Then(/the list container is deleted successfully/) {  ->
    assert secureHttpClient.delete("${baseUrl}/api/${created.id}")
}

When(/the container content is updated with {string}/) { String string ->
    String json = gson.toJson(["test", string])
    String responseJson = secureHttpClient.put("${baseUrl}/api/${created.id}/content", json)
    assert responseJson.contains(string)
    updatedContent = gson.fromJson(responseJson, Content.class)
}


Then(/the content includes {string} and {string}/) { String string, String string2 ->
    assert updatedContent.data.contains(string)
    assert updatedContent.data.contains(string2)
}