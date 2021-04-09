package com.szhua.foryou.api

import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


//suspendCancellableCoroutine
class ApiResultCallAdapterFactory :CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        //检查returnType是否是Call<T>类型的
        check(getRawType(returnType)== Call::class.java)
//        {
//            "$returnType must be retrofit2.Call."
//        }
        check(returnType is ParameterizedType) { "$returnType must be parameterized. Raw types are not supported" }
        val apiResultType = getParameterUpperBound(0, returnType)
//        check(apiResultType is ParameterizedType){
//            "$apiResultType must be parameterized. Raw types are not supported"
//        }
//        val dataType = getParameterUpperBound(0,apiResultType)
        return  ApiResultCallAdapter<Any>(apiResultType)
    }
}

class ApiResultCallAdapter<T>(private val type: Type) : CallAdapter<T, Call<T>> {
    override fun responseType(): Type = type

    override fun adapt(call: Call<T>): Call<T> {
        return ApiResultCall(call)
    }
}

class  ApiResultCall<T>(private val delegate: Call<T>):Call<T>{

    override fun enqueue(callback: Callback<T>) {
        //delegate 是用来做实际的网络请求的Call<T>对象，网络请求的成功失败会回调不同的方法
        delegate.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {//http status 是200+
                    callback.onResponse(this@ApiResultCall, response)
                } else {//http status错误
                    callback.onResponse(this@ApiResultCall, response)
                }
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
             t.printStackTrace()
            }

        })
    }

    override fun clone(): Call<T> = ApiResultCall(delegate.clone())

    override fun execute(): Response<T> {
        throw UnsupportedOperationException("ApiResultCall does not support synchronous execution")
    }

    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }

}