package com.axlecho

import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File
import java.io.InputStream
import java.io.OutputStream


class MHSpiderTask(private val url: String) {
    var status = "INIT"
    private var process = -1.0f
    private var size = -1
    private var file = File.createTempFile("tmp", ".jpg")
    private lateinit var handler: Disposable

    private var onNext: ((ResponseBody) -> Any)? = null
    private var onError: ((Throwable) -> Any)? = null
    private var onDone: ((MHSpiderTask) -> Any)? = null
    private var onProgress: ((Float) -> Any)? = null


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

    fun next(next: (ResponseBody) -> Any): MHSpiderTask {
        onNext = next
        return this
    }

    fun error(error: ((Throwable) -> Any)): MHSpiderTask {
        onError = error
        return this
    }

    fun done(done: ((MHSpiderTask) -> Any)): MHSpiderTask {
        onDone = done
        return this
    }

    fun progress(progress: ((Float) -> Any)): MHSpiderTask {
        onProgress = progress
        return this
    }

    fun execute() {
        handler = MHNetwork.INSTANCE.get(url).subscribeOn(Schedulers.io())
            .doOnNext { response ->
                size = response.byteStream().available()
                file.outputStream().use {
                    response.byteStream().copyTo(it) { totalBytesCopied, _ ->
                        process = totalBytesCopied.toFloat() / size.toFloat() * 100f
                        onProgress?.invoke(process)
                        return@copyTo process
                    }
                }
            }
            .doOnComplete { status = "DONE" }
            .doOnError { status = "FAILED" }
            .subscribe({ onNext?.invoke(it) }, { onError?.invoke(it) }, { onDone?.invoke(this) })
    }

    fun dump(): String {
        return "$process  $size  ${file.absolutePath}"
    }
}
