package com.thoughtworks.btstethoscope.components.audioplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Environment
import com.thoughtworks.btstethoscope.definitions.RECORD_FILENAME
import java.io.File


class SpeakerPlayer(private val context: Context) : AudioPlayer {

    override fun play() {
        val mp = MediaPlayer()
        mp.setDataSource(Environment.getExternalStorageDirectory().absolutePath + File.separator + RECORD_FILENAME)
        mp.setAudioAttributes(
            AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build()
        )
        mp.prepare()
        mp.start()
    }
}