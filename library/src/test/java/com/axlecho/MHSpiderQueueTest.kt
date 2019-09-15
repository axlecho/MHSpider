package com.axlecho

import com.axlecho.api.manhuadui.ManhuaduiApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class MHSpiderQueueTest {
    @Before
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun testQueue() {
        val result = ManhuaduiApi.INSTANCE.data("huiyedaxiaojiexiangrangwogaobai", "178538").blockingFirst()
        val queue = MHSpider.queue(result.data)
        queue.size()
        queue.dump()

        queue.start()
        for (i in 1..200) {
            queue.dump()
            Thread.sleep(2 * 1000)
        }

        // queue.seekTo(queue.size() / 2)
        // queue.dump()
    }

}