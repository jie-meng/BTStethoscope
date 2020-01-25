package com.thoughtworks.btstethoscope.injection

import android.app.Application
import android.content.Context
import com.thoughtworks.btstethoscope.components.audioplayer.AudioPlayer
import com.thoughtworks.btstethoscope.components.audioplayer.SpeakerPlayer
import com.thoughtworks.btstethoscope.components.audiorecorder.BluetoothVoiceAudioRecorder
import com.thoughtworks.btstethoscope.components.audiorecorder.AudioRecorder
import com.thoughtworks.btstethoscope.components.audiorecorder.AudioCache
import com.thoughtworks.btstethoscope.components.audiorecorder.AudioCacheImp
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
    fun provideAudioCache(): AudioCache {
        return AudioCacheImp()
    }

    @Singleton
    @Provides
    fun provideAudioRecorder(context: Context, audioCache: AudioCache): AudioRecorder {
        return BluetoothVoiceAudioRecorder(context, audioCache)
    }

    @Singleton
    @Provides
    fun provideAudioPlayer(context: Context, audioCache: AudioCache): AudioPlayer {
        return SpeakerPlayer(context, audioCache)
    }
}