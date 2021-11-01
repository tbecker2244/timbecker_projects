package com.example.semester_project

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.*
import java.text.ParseException


// Edit a new tour by a user
class EditActivity : Activity() {

    private lateinit var mAdapter: LocationListAdapter
    private lateinit var mLocationList: ListView
    private lateinit var mTourName: EditText
    private lateinit var mDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_tour)

        mTourName = findViewById(R.id.editName)
        mDescription = findViewById(R.id.editDesc)

        mLocationList = findViewById(R.id.listView)
        mAdapter = LocationListAdapter(applicationContext, "")

        mLocationList.setFooterDividersEnabled(true)
        val footerView = LayoutInflater.from(this).inflate(R.layout.footer_view,
            null, false)

        mLocationList.addFooterView(footerView)

        footerView.setOnClickListener {
            val tmpIntent = Intent(this, AddLocationActivity::class.java)
            startActivityForResult(tmpIntent, ADD_LOCATION_REQUEST)
        }

        mLocationList.setOnItemClickListener { _, _, position, _ ->
            val tmpIntent = Intent(this, AddLocationActivity::class.java)
            mAdapter.getItem(position).packageIntent(tmpIntent)
            tmpIntent.putExtra(NUMBER, position)
            startActivityForResult(tmpIntent, position + 1)
        }

        mLocationList.setOnItemLongClickListener { _, _, position, _ ->
            mAdapter.remove(position)
            true
        }

        mLocationList.adapter = mAdapter

        val submitButton = findViewById<View>(R.id.submitBut) as Button
        submitButton.setOnClickListener { submitNewTour() }

    }


    // submit the the Tour to firebaseDatabase and FirebaseStorage
    private fun submitNewTour() {
        if (mTourName.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please Enter the Tour Name", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (mDescription.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please Enter the Description", Toast.LENGTH_LONG)
                .show()
            return
        }

        if (mAdapter.count == 0) {
            Toast.makeText(
                this, "Please Add Locations for this Tour",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val regex = "@".toRegex()
        val author = regex.split(FirebaseAuth.getInstance().currentUser!!.email!!)[0]
        val tourName = mTourName.text.toString()
        val description = mDescription.text.toString()
        val tourReference = FirebaseDatabase.getInstance().getReference("tours")
        val id = tourReference.push().key
        if (id == null) {
            Toast.makeText(
                this, "Add Tour Failed! Please try again",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        tourReference.child(id).child("author").setValue(author)
        tourReference.child(id).child("name").setValue(tourName)
        tourReference.child(id).child("description").setValue(description)
        val locationsReference = FirebaseDatabase.getInstance()
            .getReference("locations").child(id)
        val imageReference = FirebaseStorage.getInstance()
            .getReference("locations")
        var location: Location
        var locId: String?
        var image: Uri?
        var uploadTask: UploadTask?

        for (i in 0 until mAdapter.count) {
            locId = locationsReference.push().key
            if (locId == null) {
                Toast.makeText(
                    this, "Add Tour Failed! Please try again",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            location = mAdapter.getItem(i)
            locationsReference.child(locId).child("name").setValue(location.name)
            locationsReference.child(locId).child("address").setValue(location.address)
            locationsReference.child(locId).child("description").setValue(location.description)

            for (j in location.images.indices) {
                image = location.images[j]
                uploadTask = imageReference.child(locId).child(image.lastPathSegment!! + "$j")
                    .putFile(image)

                uploadTask.addOnFailureListener {
                    Log.e(TAG, it.toString())
                }
            }
        }
        cleanFile()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == ADD_LOCATION_REQUEST && resultCode == RESULT_OK) {
            var location = data?.let { Location(it) }
            if (location != null) {
                mAdapter.add(location, "")
            }
        }

        if (requestCode != ADD_LOCATION_REQUEST && resultCode == RESULT_OK) {
            var location = data?.let { Location(it) }
            if (location != null) {
                mAdapter.adjust(location, requestCode - 1)
            }
        }

    }


    public override fun onResume() {
        super.onResume()

        if (mAdapter.count == 0)
            loadItems()
    }

    override fun onPause() {
        super.onPause()

        saveItems()

    }

    override fun onDestroy() {
        cleanFile()

        super.onDestroy()
    }

    private fun cleanFile() {
        var writer: PrintWriter? = null
        try {
            val fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            writer = PrintWriter(BufferedWriter(OutputStreamWriter(fos)))
            writer.println("")

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            writer?.close()
        }
    }

    private fun loadItems() {
        var reader: BufferedReader? = null
        try {
            val fis = openFileInput(FILE_NAME)
            reader = BufferedReader(InputStreamReader(fis))

            var address: String?
            var name: String?
            var images = ArrayList<Uri>()
            var description: String?
            var num: Int

            do {
                address = reader.readLine()
                if (address.isNullOrEmpty())
                    break
                name = reader.readLine()
                num = Integer.parseInt(reader.readLine())
                for (i in 0 until num) {
                    images.add(Uri.parse(reader.readLine()))
                }
                description = reader.readLine()
                mAdapter.add(Location(name, address, description, images), "")

            } while (true)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        } finally {
            if (null != reader) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun saveItems() {
        var writer: PrintWriter? = null
        try {
            val fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            writer = PrintWriter(BufferedWriter(OutputStreamWriter(fos)))
            for (idx in 0 until mAdapter.count) {
                writer.println(mAdapter.getItem(idx))
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            writer?.close()
        }
    }


    companion object {

        private const val ADD_LOCATION_REQUEST = 0
        private const val NUMBER = "NUMBER"
        private const val FILE_NAME = "AddTourActivityData.txt"
        private const val TAG = "Semester-Project"

    }

}