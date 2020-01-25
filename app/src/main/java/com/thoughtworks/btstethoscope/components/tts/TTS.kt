package com.thoughtworks.btstethoscope.components.tts

interface TTS {
    fun initialize(callback: (result: Boolean) -> Unit)

    fun speak(message: String)

    fun speak(message: String, finishCallback: () -> Unit)

    fun isSpeaking(): Boolean
}