package com.example.semester_project


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.IndexOutOfBoundsException


class MapActivity: AppCompatActivity(), OnMapReadyCallback {

    // Variable Declaration
    private lateinit var mMap: GoogleMap
    private lateinit var mFused: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var toolBar: Toolbar
    private lateinit var locationArray: ArrayList<String>
    private lateinit var namesArray: ArrayList<String>
    private lateinit var descriptionArray: ArrayList<String>


    //TODO: implement Map logic
    override fun onCreate(savedInstanceState: Bundle?) {
        // Set Content View to Maps Layout XML
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapslayout)

        // Get References to Tool Bar
        toolBar = findViewById(R.id.toolbar)
        toolBar.inflateMenu(R.menu.switch_activities)
        toolBar.setOnMenuItemClickListener { item ->

        // Implement Tool Bar Logic
            when (item.itemId) {
                R.id.discovery -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.user -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

        }

        // Obtain the SupportMapFragment and Launch Map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Get Location Provider Client
        mFused = LocationServices.getFusedLocationProviderClient(this)

        // Get the string array list intents from Tour detail activity
        if (intent.getStringArrayListExtra(LOCATIONS) == null)
            return

        locationArray = intent.getStringArrayListExtra(LOCATIONS)!!
        namesArray = intent.getStringArrayListExtra(NAMES)!!
        descriptionArray = intent.getStringArrayListExtra(DESCRIPTIONS)!!


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        this.mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        // Enable Google Map UI Options and Functions
        mMap.uiSettings.isZoomControlsEnabled = true

        if (ActivityCompat.checkSelfPermission(applicationContext!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {

            mMap.uiSettings.isMyLocationButtonEnabled = true

        }

        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        configureMap()
        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(applicationContext))

        // Add Locations from Tour Detail Activity into the Map
        if (intent.getStringArrayListExtra(LOCATIONS) == null)
            return

        var position = 0

        for (element in locationArray) {

            if(addressToLatLng(element).longitude == 0.0 && addressToLatLng(element).longitude == 0.0){

                Toast.makeText(this, "Not A Valid Address", Toast.LENGTH_SHORT).show()

            } else {

                // Add the marker at specified location with description and title
                addMarker(googleMap, addressToLatLng(element), namesArray[position],
                    descriptionArray[position]
                )

            }

            position += 1

        }


    }


    private fun configureMap() {

        // Check Self Permissions for getting Location
        if (ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        mMap.isMyLocationEnabled = true

        // Move camera towards current location
        mFused.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location
            if (location != null) {

                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))

            }
        }
    }


    private fun addMarker(googleMap: GoogleMap, position: LatLng, title: String, data: String) {

        mMap = googleMap

        // Add marker to map with specified title and description
        mMap.addMarker(
            MarkerOptions().position(position).snippet(title).title(data).icon(
                bitmapDescriptorFromVector(
                    applicationContext,
                    R.drawable.ic_baseline_emoji_flags_24
                )
            )
        )

    }

    // Kotlin converted code that turns a Vector Asset into Bitmap for marker icon - general outline from Stack Overflow
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {

        var drawVector = ContextCompat.getDrawable(context, vectorResId);
        drawVector!!.setBounds(0, 0, drawVector.getIntrinsicWidth(), drawVector.getIntrinsicHeight())

        var bitMap = Bitmap.createBitmap(drawVector.getIntrinsicWidth(), drawVector.getIntrinsicHeight(), Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitMap)

        drawVector.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitMap)
    }

    // Converts address string to latitude and longitude coordinates - Code outline from StackOverflow - Converted to Kotlin
    private fun addressToLatLng(strAddress: String?): LatLng {
        // Get Reference to Geocoder - converts address to Lat and Lng
        val code = Geocoder(this)
        val address: List<Address>?
        val latLng: LatLng
        val emptyLatLng = LatLng(0.0, 0.0)

        //Get latLng from Address String
        address = code.getFromLocationName(strAddress, 5)

        //check that address returned something
        if (address != null ) {

            try {
                // Grab the first most likely location and convert to LatLng
                val location = address[0]
                latLng = LatLng(location.latitude, location.longitude)

                return latLng

            } catch (e: IndexOutOfBoundsException){
                // Catches an empty array returned by Geocoder
                Toast.makeText(this, "Not A Valid Address", Toast.LENGTH_SHORT).show()
            }

        }

        return emptyLatLng

    }


    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val LOCATIONS = "LOCATIONS"
        private const val NAMES = "NAMES"
        private const val DESCRIPTIONS = "DESCRIPTIONSS"
        private const val TAG = "FinalProject"

    }


}