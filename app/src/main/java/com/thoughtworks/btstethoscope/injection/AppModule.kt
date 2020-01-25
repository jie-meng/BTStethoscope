package com.thoughtworks.btstethoscope.injection

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.thoughtworks.btstethoscope.components.audioplayer.AudioPlayer
import com.thoughtworks.btstethoscope.components.audioplayer.SpeakerPlayer
import com.thoughtworks.btstethoscope.components.audiorecorder.BluetoothAudioRecorder
import com.thoughtworks.btstethoscope.components.audiorecorder.AudioRecorder
import com.thoughtworks.btstethoscope.components.audiorecorder.InternalAudioRecorder
import com.thoughtworks.btstethoscope.components.tts.SystemTTS
import com.thoughtworks.btstethoscope.components.tts.TTS
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideContext(): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideTTS(context: Context): TTS {
        return SystemTTS(context)
    }

    @Singleton
    @Provides
    fun provideAudioRecorder(context: Context): AudioRecorder {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return if (bluetoothAdapter.isEnabled) BluetoothAudioRecorder(context) else InternalAudioRecorder(context)
    }

    @Singleton
    @Provides
    fun provideAudioPlayer(context: Context): AudioPlayer {
        return SpeakerPlayer(context)
    }
}