package com.thoughtworks.btstethoscope.utils

import android.content.Context
import android.media.MediaPlayer

object PlaySoundInAssetsUtils {

    fun playAssetSound(context: Context, soundFileName: String) {
        try {
            val mediaPlayer = MediaPlayer()

            val descriptor = context.assets.openFd(soundFileName)
            mediaPlayer.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length
            )
            descriptor.close()

            mediaPlayer.prepare()
            mediaPlayer.setVolume(1f, 1f)
            mediaPlayer.isLooping = false
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun beep(context: Context) {
        playAssetSound(context, "beep.mp3")
    }
}