package com.thoughtworks.btstethoscope.workflows

import android.Manifest
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.thoughtworks.btstethoscope.R
import com.thoughtworks.btstethoscope.definitions.State
import com.thoughtworks.btstethoscope.utils.PermissionManager
import com.thoughtworks.btstethoscope.utils.find
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private val btnStartStop by lazy { find<Button>(R.id.btn_start_stop) }
    private val spRecordDuration by lazy { find<Spinner>(R.id.sp_record_duration) }
    private val tvState by lazy { find<TextView>(R.id.tv_state) }
    private val tvAudioRecorderName by lazy { find<TextView>(R.id.tv_audio_recorder_name) }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        exitProcess(0)
    }

    private fun initPermissions() {
        PermissionManager.addRequestPermissions(
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
        PermissionManager.setRequestFailCallback { exitProcess(0) }
        PermissionManager.addRequestCallback {
            initialize()
        }
        PermissionManager.requestPermissions(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun initialize() {
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]

        viewModel.audioRecorderName.observe(this, Observer {
            tvAudioRecorderName.text = it
        })

        viewModel.isStarted.observe(this, Observer {
            btnStartStop.text = if (it) getString(R.string.stop) else getString(R.string.start)
            spRecordDuration.isEnabled = !it
        })

        viewModel.scheduleBusy.observe(this, Observer {
            btnStartStop.isEnabled = !it
        })

        viewModel.state.observe(this, Observer {
            when (it) {
                State.IDLE -> tvState.text = getString(R.string.idle)
                State.RECORDING -> tvState.text = getString(R.string.recording)
                State.PLAYING -> tvState.text = getString(R.string.playing)
            }
        })

        initUI()
    }

    private fun initUI() {
        btnStartStop.setOnClickListener {
            val seconds = Integer.parseInt(spRecordDuration.selectedItem.toString())
            viewModel.startOrStop(seconds)
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.seconds,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spRecordDuration.adapter = adapter
        }
    }
}
