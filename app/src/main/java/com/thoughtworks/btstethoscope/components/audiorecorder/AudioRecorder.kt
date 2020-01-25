package com.thoughtworks.btstethoscope.components.audiorecorder

interface AudioRecorder {
    fun start(): Pair<Boolean, String>

    fun stop()

    fun isRecording(): Boolean
}