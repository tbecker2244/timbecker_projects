package com.example.semester_project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.semester_project.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class CustomInfoWindowAdapter(): GoogleMap.InfoWindowAdapter {

    private lateinit var mWindow: View
    private lateinit var mContext: Context

    // Constructor for Custom Info Window
   constructor(context: Context?) : this() {

        if (context != null) {
           mContext = context
       }

       mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null)

   }

    // Takes the detail of the original marker (title, snippet) and puts into customized info window
    private fun updateWindowText(marker: Marker, view: View?) {

        val title = marker.title
        val snip = marker.snippet
        val windowTitle = view!!.findViewById<View>(R.id.title) as TextView
        val windowSnip = view.findViewById<View>(R.id.snippet) as TextView

        windowTitle.text = title

        windowSnip.text = snip

    }

    // Mandatory function
    override fun getInfoWindow(marker: Marker): View? {
        updateWindowText(marker, mWindow)
        return mWindow
    }

    //Mandatory function
    override fun getInfoContents(marker: Marker): View? {
        updateWindowText(marker, mWindow)
        return mWindow
    }



}