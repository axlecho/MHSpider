package com.axlecho

class MHSpiderQueue {
    fun size(): Int {
        return 0
    }

    fun dump() {}

    fun start() {}

    fun seekTo(index: Int) {}
}

class MHSpider {
    companion object {
        fun queue(data: List<String>): MHSpiderQueue {
            return MHSpiderQueue()
        }
    }
}