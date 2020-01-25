package com.thoughtworks.btstethoscope.components.audiorecorder

import android.media.MediaDataSource
import android.util.Log
import java.nio.ByteBuffer

interface AudioCache {

    fun clear()

    fun cache(byte: Byte)

    fun cache(byteArray: ByteArray, length: Int)

    fun asMediaDataSource(): MediaDataSource

    fun asByteArray(): ByteArray

    fun getSize(): Int
}

class AudioCacheImp : AudioCache {

    val bufferSize = 882000 * 2

    val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(bufferSize)

    override fun clear() {
        byteBuffer.clear()
    }

    override fun cache(byte: Byte) {
        byteBuffer.put(byte)
    }

    override fun cache(byteArray: ByteArray, length: Int) {
        Log.d("headset_rec", "writing bytes: ${length}, total: ${byteBuffer.position() + length}")
        byteBuffer.put(byteArray, 0, length)
    }

    override fun asMediaDataSource(): MediaDataSource {
        byteBuffer.flip()
        Log.d("headset_playing", "playing bytes: ${byteBuffer.limit()}")
        return ByteArrayMediaDataSource(byteBuffer.array())
    }

    override fun asByteArray(): ByteArray {
        byteBuffer.flip()
        Log.d("headset_playing", "playing bytes: ${byteBuffer.limit()}")
        return byteBuffer.array()
    }

    override fun getSize(): Int {
        return byteBuffer.position()
    }
}
