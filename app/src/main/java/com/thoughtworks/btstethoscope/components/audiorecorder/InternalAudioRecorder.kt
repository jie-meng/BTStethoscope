package com.thoughtworks.btstethoscope.components.audiorecorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import com.thoughtworks.btstethoscope.R
import com.thoughtworks.btstethoscope.definitions.RECORD_FILENAME
import java.io.File

class InternalAudioRecorder(private val context: Context) : AudioRecorder {
    override fun name(): String {
        return context.getString(R.string.internal_audio_recorder)
    }

    private val audioRecorder = com.github.piasy.rxandroidaudio.AudioRecorder.getInstance()

    private var started = false

    override fun start() {
        var audioFile =
            File(Environment.getExternalStorageDirectory().absolutePath + File.separator + RECORD_FILENAME)
        audioRecorder.prepareRecord(
            MediaRecorder.AudioSource.MIC,
            MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.AudioEncoder.AAC,
            audioFile
        )
        audioRecorder.startRecord()

        started = true
    }

    override fun stop() {
        if (!started) {
            return
        }

        audioRecorder.stopRecord()

        started = false
    }
}