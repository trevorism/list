package com.trevorism.gcloud.webapi

import com.trevorism.http.HeadersHttpResponse
import com.trevorism.http.HttpClient
import com.trevorism.https.SecureHttpClient
import com.trevorism.https.token.ObtainTokenStrategy

class EchoSecureHttpClient implements SecureHttpClient{
    @Override
    ObtainTokenStrategy getObtainTokenStrategy() {
        return null
    }

    @Override
    HttpClient getHttpClient() {
        return null
    }

    @Override
    String get(String s) {
        return s
    }

    @Override
    HeadersHttpResponse get(String s, Map<String, String> map) {
        return new HeadersHttpResponse(s, map)
    }

    @Override
    String post(String s, String s1) {
        return s1
    }

    @Override
    HeadersHttpResponse post(String s, String s1, Map<String, String> map) {
        return new HeadersHttpResponse(s1, map)
    }

    @Override
    String put(String s, String s1) {
        return s1
    }

    @Override
    HeadersHttpResponse put(String s, String s1, Map<String, String> map) {
        return new HeadersHttpResponse(s1, map)
    }

    @Override
    String patch(String s, String s1) {
        return s1
    }

    @Override
    HeadersHttpResponse patch(String s, String s1, Map<String, String> map) {
        return new HeadersHttpResponse(s1, map)
    }

    @Override
    String delete(String s) {
        return s
    }

    @Override
    HeadersHttpResponse delete(String s, Map<String, String> map) {
        return new HeadersHttpResponse(s, map)
    }
}
