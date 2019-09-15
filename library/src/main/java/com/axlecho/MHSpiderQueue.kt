package com.axlecho

import android.util.Log

class MHSpiderQueue(data: List<String>) {
    private val queue = mutableListOf<MHSpiderTask>()
    private val worker = mutableSetOf<MHSpiderTask>()
    private var index = 0

    companion object {
        private const val DEFAULT_WORKER_NUM = 5
    }

    init {
        for (url in data) {
            queue.add(MHSpiderTask(url))
        }
    }

    fun size(): Int {
        return queue.size
    }

    fun dump() {
        Log.v(TAG, "===========================================")
        for (info in queue) {
            Log.v(TAG, info.dump())
        }
        Log.v(TAG, "===========================================")
    }

    fun start() {
        while (worker.size < DEFAULT_WORKER_NUM) {
            val next = next() ?: return
            next.done {
                worker.remove(it)
                start()
            }
            worker.add(next)
            next.execute()
        }
    }

    fun next(): MHSpiderTask? {
        for (info in queue) {
            if (info.status == MHSpiderTaskStatus.INIT) {
                return info
            }
        }
        return null
    }

    fun seekTo(index: Int) {}
}
