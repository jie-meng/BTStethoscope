package com.thoughtworks.btstethoscope.components.audiorecorder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.thoughtworks.btstethoscope.definitions.APP_TAG
import com.thoughtworks.btstethoscope.definitions.SAMPLE_RATE_IN_HERTZ
import com.thoughtworks.btstethoscope.utils.AudioUtils

class BluetoothVoiceAudioRecorder(
    private val context: Context,
    private val audioCache: AudioCache
) : AudioRecorder {

    //    private val audioRecorder = com.github.piasy.rxandroidaudio.AudioRecorder.getInstance()
    private var recordingThread: Thread? = null
    private val bufferSize = AudioRecord.getMinBufferSize(
        SAMPLE_RATE_IN_HERTZ,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )
    private var audioRecord: AudioRecord? = null
    private var isRecording = false

//    private lateinit var mediaRecorder: MediaRecorder

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

                    context.unregisterReceiver(this)
                }
            }
        }, IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED))

        return Pair(true, "")
    }

    private fun recorderStart() {
//        mediaRecorder = MediaRecorder()
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//        mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().absolutePath + File.separator + "record.mp3")
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//
//        mediaRecorder.prepare()
//        mediaRecorder.start()

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE_IN_HERTZ,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize * 10
        )
        audioRecord!!.startRecording()
        isRecording = true
        recordingThread = Thread(Runnable { writeAudioData() }, "AudioRecorder Thread")
        recordingThread!!.start()
    }

    override fun stop() {
//        mediaRecorder.stop()
//        mediaRecorder.release()

        if (null != audioRecord) {
            isRecording = false
            audioRecord!!.stop()
            audioRecord!!.release()
            audioRecord = null
            recordingThread = null
        }

        isRecording = false
        AudioUtils.changeToSpeaker(context)
    }

    private fun writeAudioData() {
        val audioBuffer = ByteArray(bufferSize)
        while (isRecording) {
            val readResult = audioRecord!!.read(audioBuffer, 0, bufferSize)
            when (readResult) {
                AudioRecord.ERROR_INVALID_OPERATION, AudioRecord.ERROR_BAD_VALUE, AudioRecord.ERROR_DEAD_OBJECT, AudioRecord.ERROR ->
                    Log.d("AudioRecord", "read error: $readResult")
                else -> audioCache.cache(audioBuffer, readResult)
            }
        }
    }

    override fun isRecording(): Boolean {
        return isRecording
    }
}