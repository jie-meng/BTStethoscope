package com.thoughtworks.btstethoscope.workflows

import android.Manifest
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.thoughtworks.btstethoscope.R
import com.thoughtworks.btstethoscope.utils.PermissionManager
import com.thoughtworks.btstethoscope.utils.find
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private val btnStartStop by lazy { find<Button>(R.id.btn_start_stop) }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPermissions()
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

        viewModel.isStarted.observe(this, Observer {
            btnStartStop.text = if (it) getString(R.string.stop) else getString(R.string.start)
        })

        initUI()
    }

    private fun initUI() {
        btnStartStop.setOnClickListener {
            viewModel.startOrStop()
        }
    }
}
