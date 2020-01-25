package com.thoughtworks.btstethoscope.components.tts

import android.content.Context
import android.os.SystemClock
import android.speech.tts.TextToSpeech
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*

class SystemTTS(private val context: Context) : TTS {
    private lateinit var tts: TextToSpeech

    override fun initialize(callback: (result: Boolean) -> Unit) {
        tts = TextToSpeech(context, TextToSpeech.OnInitListener {
            callback(it == TextToSpeech.SUCCESS)
        })

        tts.language = Locale.CHINA
    }

    override fun speak(message: String) {
        Single.create<Object> {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null)
            it.onSuccess(Object())
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun speak(message: String, finishCallback: () -> Unit) {

        Single.create<Object> {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null)

            while (true) {
                SystemClock.sleep(100)
                if (!tts.isSpeaking) {
                    break
                }
            }

            finishCallback()
            it.onSuccess(Object())
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun isSpeaking(): Boolean {
        return tts.isSpeaking
    }
}