package com.thoughtworks.btstethoscope.components.audiorecorder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import com.thoughtworks.btstethoscope.definitions.APP_TAG
import com.thoughtworks.btstethoscope.utils.AudioUtils
import java.io.File

class BluetoothVoiceAudioRecorder(private val context: Context) : AudioRecorder {

    private val audioRecorder = com.github.piasy.rxandroidaudio.AudioRecorder.getInstance()

    private lateinit var mediaRecorder: MediaRecorder

    override fun start(): Pair<Boolean, String> {
        val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (!manager.isBluetoothScoAvailableOffCall) {
            return Pair(false, "System does not support bluetooth record")
        }

        manager.startBluetoothSco()

        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val state = intent!!.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1)
                if (state == AudioManager.SCO_AUDIO_STATE_CONNECTED) {
                    Log.i(APP_TAG, "Bluetooth SOC connected")

                    AudioUtils.changeToBluetooth(context!!)

                    recorderStart()

                    context?.unregisterReceiver(this)
                }
            }
        }, IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED))

        return Pair(true, "")
    }

    private fun recorderStart() {
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().absolutePath + File.separator + "record.mp3")
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        mediaRecorder.prepare()
        mediaRecorder.start()
    }

    override fun stop() {
        mediaRecorder.stop()
        mediaRecorder.release()

        AudioUtils.changeToSpeaker(context!!)
    }

    override fun isRecording(): Boolean {
        return audioRecorder.isStarted
    }
}