package com.thoughtworks.btstethoscope.components.audiorecorder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import com.thoughtworks.btstethoscope.R
import com.thoughtworks.btstethoscope.definitions.APP_TAG
import com.thoughtworks.btstethoscope.definitions.RECORD_FILENAME
import com.thoughtworks.btstethoscope.utils.AudioUtils
import java.io.File

class BluetoothAudioRecorder(private val context: Context) : AudioRecorder {
    override fun name(): String {
        return context.getString(R.string.bluetooth_audio_recorder)
    }

    private lateinit var mediaRecorder: MediaRecorder

    private var started = false

    override fun start() {
        val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (!manager.isBluetoothScoAvailableOffCall) {
            return
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

        return
    }

    private fun recorderStart() {
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().absolutePath + File.separator + RECORD_FILENAME)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        mediaRecorder.prepare()
        mediaRecorder.start()

        started = true
    }

    override fun stop() {
        if (!started) {
            return
        }

        mediaRecorder.stop()
        mediaRecorder.release()

        AudioUtils.changeToSpeaker(context!!)

        started = false
    }
}