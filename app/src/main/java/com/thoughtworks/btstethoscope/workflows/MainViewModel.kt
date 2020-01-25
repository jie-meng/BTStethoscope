package com.thoughtworks.btstethoscope.workflows

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.thoughtworks.btstethoscope.App
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val tts = (application as App).appComponent.getTTS()
    private val recorder = (application as App).appComponent.getAudioRecorder()
    private val player = (application as App).appComponent.getAudioPlayer()

    var isTTSOK = MutableLiveData(false)
    var isStarted = MutableLiveData(false)

    private val compositeDisposable = CompositeDisposable()

    init {
        tts.initialize { isTTSOK.postValue(it) }
    }

    fun startOrStop() {
        if (!isStarted.value!!) {
            start()
        } else {
            stop()
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    private fun start(): Pair<Boolean, String> {
        val start = recorder.start()
        isStarted.postValue(start.first)
        return start
    }

    private fun stop() {
        recorder.stop()
        isStarted.postValue(false)

        player.play()
    }
}
