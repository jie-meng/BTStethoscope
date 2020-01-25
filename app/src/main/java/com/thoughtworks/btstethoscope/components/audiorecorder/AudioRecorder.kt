package com.thoughtworks.btstethoscope.components.audiorecorder

interface AudioRecorder {
    fun name(): String

    fun start()

    fun stop()
}