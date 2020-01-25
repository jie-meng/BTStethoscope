package com.thoughtworks.btstethoscope.components.audioplayer

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Environment
import com.thoughtworks.btstethoscope.utils.StreamAudioPlayerEx
import java.io.File


class SpeakerPlayer(private val context: Context) : AudioPlayer {

//    private val streamAudioPlayer = StreamAudioPlayerEx.getInstance()

    override fun play() {
//        streamAudioPlayer.init()

        try {
            var mp = MediaPlayer()
            mp.setDataSource(Environment.getExternalStorageDirectory().absolutePath + File.separator + "record.mp3")
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mp.prepare()
            mp.start()

//            val inputStream = FileInputStream(Environment.getExternalStorageDirectory().absolutePath + File.separator + "record.mp3")
//
//            val buffer = ByteArray(StreamAudioRecorder.DEFAULT_BUFFER_SIZE)
//            var read = inputStream.read(buffer)
//            while (read > 0) {
//                streamAudioPlayer.play(buffer, read)
//                inputStream.read(buffer)
//            }
//            inputStream.close()
//            streamAudioPlayer.release()
        } catch (e: Exception) {

        }
    }
}