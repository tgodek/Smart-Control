package com.example.smartcontrol

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlin.collections.ArrayList

class PrikazBluetoothUredaja(private val context: Activity, private val ime: ArrayList<String>, private val slikaUredaja: ArrayList<Drawable>, private val stavke: ArrayList<String>) :
    ArrayAdapter<String>(context,R.layout.layout_adapter,stavke){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.layout_adapter, null, true)

        val btIme: TextView = view.findViewById(R.id.txtImeUredaja)
        val btSlika: ImageView = view.findViewById(R.id.imgKlaseUredaja)

        btIme.text = ime[position]
        btSlika.setImageDrawable(slikaUredaja[position])

        return view
    }

}
