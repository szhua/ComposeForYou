package com.szhua.foryou.api

import com.szhua.foryou.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * 全局拦截器
 * 为网络请求添加Header
 */
class BMobHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("content-type","application/json")
            .addHeader("X-Bmob-Application-Id", BuildConfig.BMOB_APPLICATION_ID)
            .addHeader("X-Bmob-REST-API-Key", BuildConfig.BMOB_REST_API_KEY)
            .build()
        return  chain.proceed( request)
    }
}