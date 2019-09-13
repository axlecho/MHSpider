package com.axlecho

import android.util.Log
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.InputStream
import java.io.OutputStream

const val TAG = "MHSpider"

class MHSpiderInfo(private val url: String) {
    var status = "INIT"
    private var process = -1.0f
    private var size = -1
    private var file = File.createTempFile(".jpg", "tmp ")
    private lateinit var handler: Disposable

    private fun InputStream.copyTo(out: OutputStream, onCopy: (totalBytesCopied: Long, bytesJustCopied: Int) -> Any): Long {
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

    fun execute(onDone: () -> Any) {
        handler = MHNetwork.INSTANCE.get(url).subscribeOn(Schedulers.io())
            .doOnNext { response ->
                size = response.byteStream().available()
                file.outputStream().use {
                    response.byteStream().copyTo(it) { totalBytesCopied, _ ->
                        process = totalBytesCopied.toFloat() / size.toFloat() * 100f
                        return@copyTo process
                    }
                }
            }
            .doOnComplete { status = "DONE" }
            .doOnError { status = "FAILED" }
            .subscribe { onDone() }
    }

    fun dump(): String {
        return "$process  $size  ${file.absolutePath}"
    }
}

class MHSpiderQueue(data: List<String>) {
    private val queue = mutableListOf<MHSpiderInfo>()
    private val worker = mutableSetOf<MHSpiderInfo>()
    private var index = 0

    companion object {
        private const val DEFAULT_WORKER_NUM = 5
    }

    init {
        for (url in data) {
            queue.add(MHSpiderInfo(url))
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
            next.execute { start() }
            worker.add(next)
        }
    }

    fun next(): MHSpiderInfo? {
        for (info in queue) {
            if (info.status == "DONE") continue
            return info
        }
        return null
    }

    fun seekTo(index: Int) {}
}

class MHSpider {
    companion object {
        fun queue(data: List<String>): MHSpiderQueue {
            return MHSpiderQueue(data)
        }
    }
}