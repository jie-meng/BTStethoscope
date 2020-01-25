package com.thoughtworks.btstethoscope.workflows

import android.app.Application
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.thoughtworks.btstethoscope.App
import com.thoughtworks.btstethoscope.definitions.APP_TAG
import com.thoughtworks.btstethoscope.definitions.State
import com.thoughtworks.btstethoscope.utils.VibrateUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val tts = (application as App).appComponent.getTTS()
    private val recorder = (application as App).appComponent.getAudioRecorder()
    private val player = (application as App).appComponent.getAudioPlayer()

    private var isTTSOK = MutableLiveData(false)
    var isStarted = MutableLiveData(false)
    var state = MutableLiveData(State.IDLE)
    var scheduleBusy = MutableLiveData(false)

    private val compositeDisposable = CompositeDisposable()

    init {
        tts.initialize { isTTSOK.postValue(it) }
    }

    fun startOrStop(seconds: Int) {
        if (!isStarted.value!!) {
            startSchedule(seconds)
        } else {
            stopSchedule()
        }
    }

    override fun onCleared() {
        if (isStarted.value!!) {
            stopSchedule()
        }

        super.onCleared()
    }

    private fun startSchedule(seconds: Int) {
        isStarted.value = true

        Observable.create<Object> { emitter ->
            while (isStarted.value!!) {
                emitter.onNext(Object())
                SystemClock.sleep(seconds * 1000L)
            }

            emitter.onComplete()
            scheduleBusy.postValue(false)

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Object> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(t: Object) {
                    if (state.value == State.IDLE || state.value == State.PLAYING) {
                        record()
                    } else {
                        play()
                    }
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    private fun stopSchedule() {
        if (state.value == State.RECORDING) {
            recorder.stop()
        }

        if (state.value == State.PLAYING) {
            player.stop()
        }

        state.postValue(State.IDLE)

        scheduleBusy.postValue(true)
        isStarted.postValue(false)
    }

    private fun record() {
        VibrateUtils.vibrate(getApplication(), 300)
        player.stop()
        recorder.start()
        state.postValue(State.RECORDING)
    }

    private fun play() {
        recorder.stop()
        VibrateUtils.vibrate(getApplication(), 100)
        player.start()
        state.postValue(State.PLAYING)
    }
}