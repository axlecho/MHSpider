package com.axlecho

const val TAG = "MHSpider"

class MHSpider {
    companion object {
        fun queue(data: List<String>): MHSpiderQueue {
            return MHSpiderQueue(data)
        }
    }
}