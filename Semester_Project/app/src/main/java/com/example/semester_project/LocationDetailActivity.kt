package com.example.semester_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import java.io.File


// The activity to show the detail of a location
class LocationDetailActivity: Activity() {
    private lateinit var nameView: TextView
    private lateinit var addressView: TextView
    private lateinit var descView: TextView
    private lateinit var imageRecyclerView: RecyclerView
    private lateinit var mAdapter: LocationImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_detail)
        nameView = findViewById(R.id.locationName)
        addressView = findViewById(R.id.locationAddress)
        descView = findViewById(R.id.locationDesc)

        nameView.text = intent.getStringExtra(NAME)
        addressView.text = intent.getStringExtra(LOCATION_ADDRESS)
        descView.text = intent.getStringExtra(DESCRIPTION)

        //Set RecycleView for images
        imageRecyclerView = findViewById(R.id.locationImageRecycleView)
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        imageRecyclerView.layoutManager = layoutManager
        mAdapter = LocationImageAdapter(applicationContext)
        imageRecyclerView.adapter = mAdapter

        //Set show location on map button
        val showBut = findViewById<Button>(R.id.showLocationBut)
        showBut.setOnClickListener{
            val tmpIntent = Intent(this, MapActivity::class.java)
            val nameArray = ArrayList<String>()
            nameArray.add(intent.getStringExtra(NAME)!!)
            tmpIntent.putExtra(NAMES, nameArray)
            val locArray = ArrayList<String>()
            locArray.add(intent.getStringExtra(LOCATION_ADDRESS)!!)
            tmpIntent.putExtra(LOCATIONS, locArray)
            val descArray = ArrayList<String>()
            descArray.add(intent.getStringExtra(DESCRIPTION)!!)
            tmpIntent.putExtra(DESCRIPTIONS, descArray)
            startActivity(tmpIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        val imageReference = FirebaseStorage.getInstance()
        .getReference("locations").child(intent.getStringExtra(LOCATION_ID)!!)
        imageReference.listAll()
            .addOnSuccessListener { (items, _) ->
                items.forEach { item ->
                    item.downloadUrl.addOnCompleteListener {
                        mAdapter.add(it.result)
                    }
                }
            }.addOnFailureListener {
                Log.e(TAG, it.toString())
            }
    }




    companion object {
        private const val LOCATION_ADDRESS = "LOCATION_ADDRESS"
        private const val IMAGES = "IMAGES"
        private const val DESCRIPTION = "DESCRIPTION"
        private const val NAME = "NAME"
        private const val LOCATION_ID = "LOCATION_ID"
        private const val LOCATIONS = "LOCATIONS"
        private const val NAMES = "NAMES"
        private const val DESCRIPTIONS = "DESCRIPTIONSS"
        private const val TAG = "Semester-Project"

    }
}