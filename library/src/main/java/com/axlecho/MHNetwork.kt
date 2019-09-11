package com.axlecho

import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface NetInterface {
    @GET
    fun raw(@Url url: String): Observable<ResponseBody>
}

class MHNetwork private constructor() {
    companion object {
        val INSTANCE: MHNetwork by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MHNetwork()
        }
    }


    private val site: NetInterface

    init {
        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        builder.addNetworkInterceptor(logging)
        site = Retrofit.Builder()
            .client(builder.build())
            .baseUrl("https://www.baidu.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NetInterface::class.java)
    }

    fun get(url: String): Observable<ResponseBody> {
        return site.raw(url)
    }
}