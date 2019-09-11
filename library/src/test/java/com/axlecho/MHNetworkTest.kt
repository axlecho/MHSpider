package com.axlecho

import android.util.Log
import com.axlecho.api.manhuadui.ManhuaduiApi
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
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

            if(i == 10 && !ret.isDisposed) {
                ret.dispose()
            }

            Log.d("NetworkTest", "network is disposed - " + ret.isDisposed)
            Thread.sleep(200)
        }
    }

    @Test
    fun testData() {
        val result = ManhuaduiApi.INSTANCE.data("huiyedaxiaojiexiangrangwogaobai", "178538").blockingFirst()
        Log.d("test",result.toString())
        Assert.assertEquals(23, result.data.size)
        Assert.assertEquals("https://mhcdn.manhuazj.com/ManHuaKu/h/huiyedaxiaojiexiangrangwogaobaitiancaimendelianait/1/201944768.jpg", result.data[0])
    }
}