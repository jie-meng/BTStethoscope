package com.thoughtworks.btstethoscope.injection

import android.content.Context
import com.thoughtworks.btstethoscope.components.audioplayer.AudioPlayer
import com.thoughtworks.btstethoscope.components.audiorecorder.AudioRecorder
import com.thoughtworks.btstethoscope.components.audiorecorder.AudioCache
import com.thoughtworks.btstethoscope.components.tts.TTS
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun getContext(): Context
    fun getTTS(): TTS
    fun getAudioRecorder(): AudioRecorder
    fun getAudioPlayer(): AudioPlayer
}