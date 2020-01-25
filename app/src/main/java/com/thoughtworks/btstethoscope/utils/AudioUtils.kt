package com.thoughtworks.btstethoscope.utils

import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.AUDIOFOCUS_GAIN
import android.os.Build


object AudioUtils {
    private var lastModel = -10
    /**
     * 音频外放
     */
    fun changeToSpeaker(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        //注意此处，蓝牙未断开时使用MODE_IN_COMMUNICATION而不是MODE_NORMAL
        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
        audioManager.stopBluetoothSco()
        audioManager.isBluetoothScoOn = false
        audioManager.isSpeakerphoneOn = true
    }

    /**
     * 切换到蓝牙音箱
     */
    fun changeToBluetooth(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
        audioManager.startBluetoothSco()
        audioManager.isBluetoothScoOn = true
        audioManager.isSpeakerphoneOn = false
    }

    /**
     * 切换到听筒
     */
    fun changeToReceiver(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.isSpeakerphoneOn = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
        } else {
            audioManager.mode = AudioManager.MODE_IN_CALL
        }
    }


    fun dispose(context: Context, focusRequest: AudioManager.OnAudioFocusChangeListener?) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.mode = lastModel
        if (audioManager.isBluetoothScoOn) {
            audioManager.isBluetoothScoOn = false
            audioManager.stopBluetoothSco()
        }
        audioManager.unloadSoundEffects()
        if (null != focusRequest) {
            audioManager.abandonAudioFocus(focusRequest)
        }
    }


    fun getModel(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        lastModel = audioManager.mode
    }

    fun changeToNomal(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.mode = AudioManager.MODE_NORMAL
    }

    fun isWiredHeadsetOn(context: Context): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.isWiredHeadsetOn
    }

    fun isBluetoothA2dpOn(context: Context): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.isBluetoothA2dpOn
    }

    /**
     * context 传入的是MicroContext.getApplication()
     * @param context
     */
    fun choiceAudioModel(context: Context) {
        if (isWiredHeadsetOn(context)) {
            changeToReceiver(context)
        } else if (isBluetoothA2dpOn(context)) {
            changeToBluetooth(context)
        } else {
            changeToSpeaker(context)
        }
    }

    fun pauseMusic(context: Context, focusRequest: AudioManager.OnAudioFocusChangeListener) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.requestAudioFocus(focusRequest, AudioManager.STREAM_MUSIC, AUDIOFOCUS_GAIN)
    }


}
