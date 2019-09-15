package com.axlecho

import android.util.Log
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class MHSpiderTaskTest {
    companion object {
        private const val TEST_TAG = "MHSpiderTest"
    }

    private val task = MHSpiderTask("https://mov.bn.netease.com/open-movie/nos/mp4/2014/06/24/S9UJAG3QE_sd.mp4")

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun testTaskExecute() {
        task.done { Assert.assertTrue(MHSpiderTaskStatus.DONE == task.status) }
            .progress { Log.v(TEST_TAG, task.dump()) }
            .error { Log.e(TEST_TAG, "task error $it") }
            .next { }

        task.execute()
        Assert.assertTrue(MHSpiderTaskStatus.RUNNING == task.status)
        Thread.sleep(40000)
    }

    @Test
    fun testTaskCancel() {


        task.done { Log.v(TEST_TAG, "task done") }
            .progress { Log.v(TEST_TAG, "task progress $it") }
            .next { }
            .error { Assert.assertTrue(it.message == "cancel") }

        task.execute()
        Thread.sleep(10000)
        task.cancel()
        Assert.assertTrue(MHSpiderTaskStatus.CANCEL == task.status)
        Thread.sleep(40000)
    }
}