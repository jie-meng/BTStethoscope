package com.thoughtworks.btstethoscope.components.audiorecorder

import android.annotation.TargetApi
import android.media.MediaDataSource
import android.os.Build
import java.io.IOException


@TargetApi(Build.VERSION_CODES.M)
class ByteArrayMediaDataSource(private val data: ByteArray) : MediaDataSource() {

    @Throws(IOException::class)
    override fun readAt(
        position: Long,
        buffer: ByteArray,
        offset: Int,
        size: Int
    ): Int {
        System.arraycopy(data, position.toInt(), buffer, offset, size)
        return size
    }

    @Throws(IOException::class)
    override fun getSize(): Long {
        return data.size.toLong()
    }

    @Throws(IOException::class)
    override fun close() { // Nothing to do here
    }
}