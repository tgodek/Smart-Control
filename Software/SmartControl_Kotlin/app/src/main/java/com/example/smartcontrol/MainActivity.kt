package com.example.smartcontrol

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var uparen_uredaj: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1

    companion object {
        val EXTRA_ADDRESS: String = "adresa_uredaja"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(bluetoothAdapter == null) {
            toast("Ovaj uređaj ne podržava Bluetooth")
            return
        }
        if(!bluetoothAdapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        btnOsvjeziPopis.setOnClickListener{ listaUperanihUredaja() }
    }


    private fun listaUperanihUredaja() {
        var btIme : ArrayList<String> = ArrayList()
        var btSlika : ArrayList<Drawable> = ArrayList()
        var btAdresa : ArrayList<String> = ArrayList()
        uparen_uredaj = bluetoothAdapter!!.bondedDevices

        if (!uparen_uredaj.isEmpty()) {
            for (device: BluetoothDevice in uparen_uredaj) {
                btIme.add(device.name)
                btAdresa.add(device.address)
                btSlika.add(dohvatiSlikuKlaseUredaja
                    (device.bluetoothClass.majorDeviceClass)!!)
                Log.i("device", "" + device)
            }
        } else {
            toast("Nije pronađen niti jedan bluetooth uređaj")
        }

        val adapter = PrikazBluetoothUredaja(this, btIme, btSlika, btAdresa)
        lvPopisUredaja.adapter = adapter
        lvPopisUredaja.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val adresa = btAdresa[position]
            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, adresa)
            startActivity(intent)
        }
    }

    private fun dohvatiSlikuKlaseUredaja(tip: Int): Drawable? {
        return when (tip) {
            BluetoothClass.Device.Major.COMPUTER -> ContextCompat.getDrawable(
                this,
                R.drawable.ic_smart_device
            )
            BluetoothClass.Device.Major.PHONE -> ContextCompat.getDrawable(
                this,
                R.drawable.ic_phone
            )
            else -> ContextCompat.getDrawable(this, R.drawable.ic_smart_device)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (bluetoothAdapter!!.isEnabled) {
                    toast("Bluetooth je uključen")
                } else {
                    toast("Bluetooth je isključen")
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                toast("Zaustavljeno je uključivanje bluetootha")
            }
        }
    }
}