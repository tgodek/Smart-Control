package com.example.smartcontrol

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.control_layout.*
import java.io.IOException
import java.util.*

class ControlActivity: AppCompatActivity() {

    companion object {
        var moj_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var bluetoothSocket: BluetoothSocket? = null
        var povezan: Boolean = false
        lateinit var adresa: String
        lateinit var bluetoothAdapter: BluetoothAdapter
        lateinit var napredak: ProgressDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.control_layout)
        adresa = intent.getStringExtra(MainActivity.EXTRA_ADDRESS)

        KonekcijaUredaja(this).execute()

        motor_on.setOnClickListener { PosaljiNaredbu("upali") }
        motor_off.setOnClickListener { PosaljiNaredbu("ugasi") }
        motor_disconnect.setOnClickListener { IskljuciKonekciju() }
    }

    private fun PosaljiNaredbu(input: String) {
        if (bluetoothSocket != null) {
            try{
                bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun IskljuciKonekciju() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket!!.close()
                bluetoothSocket = null
                povezan = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }

    private class KonekcijaUredaja(c: Context) : AsyncTask<Void, Void, String>() {
        private var uspjesno: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            napredak = ProgressDialog.show(context, "Povezivam...", "molimo pričekajte")
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (bluetoothSocket == null || !povezan) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(adresa)
                    bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(moj_UUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                uspjesno = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!uspjesno) {
                Log.i("data", "nije moguće povezati")
            } else {
                povezan = true
            }
            napredak.dismiss()
        }
    }
}