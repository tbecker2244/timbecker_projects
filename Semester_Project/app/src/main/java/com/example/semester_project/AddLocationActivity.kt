package com.example.semester_project

import android.app.*
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.collections.ArrayList

// add Location towards a new tour
// Support later edition for the location
class AddLocationActivity: Activity() {

    private lateinit var mAddImage1: Button
    private lateinit var mAddImage2: Button
    private lateinit var mAddImage3: Button
    private lateinit var mImage1: ImageView
    private lateinit var mImage2: ImageView
    private lateinit var mImage3: ImageView
    private lateinit var nameView: EditText
    private lateinit var addressView: EditText
    private lateinit var descView: EditText
    private val images = ArrayList<Uri>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_location)

        nameView = findViewById(R.id.editName)
        addressView = findViewById(R.id.editAddress)
        descView = findViewById(R.id.editDesc)

        val cancelButton = findViewById<View>(R.id.cancelLoactionBut) as Button
        cancelButton.setOnClickListener {
            Log.i(TAG, "Entered cancelButton.OnClickListener.onClick()")

            val tmpIntent = Intent()
            setResult(RESULT_CANCELED, tmpIntent)
            finish()
        }

        mAddImage1 = findViewById(R.id.addImageBut1)
        mAddImage2 = findViewById(R.id.addImageBut2)
        mAddImage3 = findViewById(R.id.addImageBut3)

        mAddImage1.setOnClickListener { requestImage(IMAGE_REQUEST_1) }
        mAddImage2.setOnClickListener { requestImage(IMAGE_REQUEST_2) }
        mAddImage3.setOnClickListener { requestImage(IMAGE_REQUEST_3) }

        mImage1 = findViewById(R.id.image1)
        mImage2 = findViewById(R.id.image2)
        mImage3 = findViewById(R.id.image3)

        val submitButton = findViewById<View>(R.id.submitLoactionBut) as Button
        submitButton.setOnClickListener { submit() }

        if (intent.getIntExtra(NUMBER, -1) != -1) {
            setUI()
        }
    }

    // handle the case when author want to edit the location added beforehand
    private fun setUI() {
        nameView.setText(intent.getStringExtra(NAME))
        addressView.setText(intent.getStringExtra(LOCATION_ADDRESS))
        descView.setText(intent.getStringExtra(DESCRIPTION))
        val photos = intent.getParcelableArrayListExtra<Uri>(IMAGES)
        if (photos != null)
            for (i in photos.indices) {
                images.add(photos[i])
                when (i) {
                    0 -> mImage1.setImageURI(photos[0])
                    1 -> mImage2.setImageURI(photos[1])
                    2 -> mImage3.setImageURI(photos[2])
                }
            }
    }

    // submit the location
    private fun submit() {
        val name = nameView.text.toString()
        val address = addressView.text.toString()
        val description = descView.text.toString()
        if (name.isNullOrEmpty()) {
            Toast.makeText(this, "Please Enter Location Name", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (address.isNullOrEmpty()) {
            Toast.makeText(this, "Please Enter Location Address", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (description.isNullOrEmpty()) {
            Toast.makeText(this, "Please Enter Location Description",
                Toast.LENGTH_LONG).show()
            return
        }

        val tmpIntent = Intent()
        tmpIntent.putExtra(NAME, name)
        tmpIntent.putExtra(LOCATION_ADDRESS, address)
        tmpIntent.putExtra(DESCRIPTION, description)
        while (Uri.parse("") in images)
            images.remove(Uri.parse(""))
        tmpIntent.putExtra(IMAGES, images)

        setResult(RESULT_OK, tmpIntent)
        Log.i(TAG, name + address + description)
        finish()
    }

    private fun requestImage(requestCode: Int) {
        val tmpIntent = Intent(
            Intent.ACTION_OPEN_DOCUMENT,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(tmpIntent, requestCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.i(TAG, "Entered onActivityResult()")

        if (requestCode == IMAGE_REQUEST_1 && resultCode == RESULT_OK) {
            val selectedImage = data?.let { it.data }
            if (selectedImage != null) {
                try {
                    if (images.size == 0)
                        images.add(selectedImage)
                    else
                        images[0] = selectedImage
                    mImage1.setImageURI(selectedImage)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        if (requestCode == IMAGE_REQUEST_2 && resultCode == RESULT_OK) {
            val selectedImage = data?.let { it.data }
            if (selectedImage != null) {
                try {
                    when(images.size) {
                        0 -> {
                            images.add(Uri.parse(""))
                            images.add(selectedImage)
                        }
                        1 -> images.add(selectedImage)
                        else -> images[1] = selectedImage
                    }
                    mImage2.setImageURI(selectedImage)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        if (requestCode == IMAGE_REQUEST_3 && resultCode == RESULT_OK) {
            val selectedImage = data?.let { it.data }
            if (selectedImage != null) {
                try {
                    when(images.size) {
                        0 -> {
                            images.add(Uri.parse(""))
                            images.add(Uri.parse(""))
                            images.add(selectedImage)
                        }
                        1 -> {
                            images.add(Uri.parse(""))
                            images.add(selectedImage)
                        }
                        2 -> images.add(selectedImage)
                        3 -> images[2] = selectedImage
                    }
                    mImage3.setImageURI(selectedImage)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    }


    companion object {

        private const val LOCATION_ADDRESS = "LOCATION_ADDRESS"
        private const val IMAGES = "IMAGES"
        private const val DESCRIPTION = "DESCRIPTION"
        private const val NAME = "NAME"
        private const val NUMBER = "NUMBER"
        private const val IMAGE_REQUEST_1 = 0
        private const val IMAGE_REQUEST_2 = 1
        private const val IMAGE_REQUEST_3 = 2
        private const val TAG = "Semester-Project"

    }
}
