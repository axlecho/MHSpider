package com.axlecho

import android.util.Log
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class MHNetworkTest {

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun testGet() {
        val ret = MHNetwork.INSTANCE.get("http://hiphotos.baidu.com/feed/pic/item/6f061d950a7b020832cac7c06ed9f2d3572cc894.gif").subscribeOn(Schedulers.io()).subscribe {
            Log.d("NetworkTest", "network call back")
        }

        for (i in 1..100) {

            if(i == 20 && !ret.isDisposed) {
                ret.dispose()
            }

            Log.d("NetworkTest", "network is disposed - " + ret.isDisposed)
            Thread.sleep(200)
        }

    }
}