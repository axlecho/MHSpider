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
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@RunWith(RobolectricTestRunner::class)
class MHNetworkTest {

    fun InputStream.copyTo(out: OutputStream, onCopy: (totalBytesCopied: Long, bytesJustCopied: Int) -> Any): Long {
        var bytesCopied: Long = 0
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytes = read(buffer)
        while (bytes >= 0) {
            out.write(buffer, 0, bytes)
            bytesCopied += bytes
            onCopy(bytesCopied, bytes)
            bytes = read(buffer)
        }
        return bytesCopied
    }

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun testGet() {
        val ret =
            MHNetwork.INSTANCE.get("http://hiphotos.baidu.com/feed/pic/item/6f061d950a7b020832cac7c06ed9f2d3572cc894.gif")
                .subscribeOn(Schedulers.io()).subscribe {
                    Log.d("NetworkTest", "network call back")
                }

        for (i in 1..100) {

            if (i == 10 && !ret.isDisposed) {
                ret.dispose()
            }

            Log.d("NetworkTest", "network is disposed - " + ret.isDisposed)
            Thread.sleep(200)
        }
    }

    @Test
    fun testDownload() {
        val ret = MHNetwork.INSTANCE.get("https://flv2.bn.netease.com/videolib1/1810/12/mMEjO280t/SD/mMEjO280t-mobile.mp4").blockingFirst()
        val file = File.createTempFile(".mp4", "tmp")
        file.outputStream().use {
            ret.byteStream().copyTo(it, onCopy = { totalBytesCopied, _ ->
                Log.d("NetworkTest", "download $totalBytesCopied")
            })
        }

        Assert.assertTrue(file.exists())
        Assert.assertTrue(file.isFile)
        Assert.assertTrue(file.length() > 0L)
    }

    @Test
    fun testData() {
        val result = ManhuaduiApi.INSTANCE.data("huiyedaxiaojiexiangrangwogaobai", "178538").blockingFirst()
        Log.d("test", result.toString())
        Assert.assertEquals(23, result.data.size)
        Assert.assertEquals(
            "https://mhcdn.manhuazj.com/ManHuaKu/h/huiyedaxiaojiexiangrangwogaobaitiancaimendelianait/1/201944768.jpg",
            result.data[0]
        )
    }

    @Test
    fun testQueue() {
        val result = ManhuaduiApi.INSTANCE.data("huiyedaxiaojiexiangrangwogaobai", "178538").blockingFirst()
        val queue = MHSpider.queue(result.data)
        queue.size()
        queue.dump()

        queue.start()
        for (i in 1..20) {
            queue.dump()
            Thread.sleep(2 * 1000)
        }

        // queue.seekTo(queue.size() / 2)
        // queue.dump()
    }
}