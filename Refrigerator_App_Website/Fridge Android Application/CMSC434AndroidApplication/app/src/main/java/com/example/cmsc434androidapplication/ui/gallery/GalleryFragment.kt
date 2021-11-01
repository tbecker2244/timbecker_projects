package com.example.cmsc434androidapplication.ui.gallery

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmsc434androidapplication.MyRecyclerViewAdapter
import com.example.cmsc434androidapplication.R
import javax.microedition.khronos.egl.EGLDisplay


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var alertDialog2: AlertDialog.Builder

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProvider(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val mRecyclerView = root.findViewById<RecyclerView>(R.id.list)

        //Set the layout manager
        mRecyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)

        // Set up the adapter
        var foodNames = ArrayList<String>()
        var foodItems = ArrayList<String>()
        var quanityList = ArrayList<String>()

        val removeButton: Button = root.findViewById(R.id.clear_list)
        val addButton: Button = root.findViewById(R.id.add_food_button)

        removeButton.setBackgroundColor(resources.getColor(R.color.red))
        addButton.setBackgroundColor(resources.getColor(R.color.green))

        addButton.setOnClickListener{

            val droot = inflater.inflate(R.layout.add_food_dialog, container, false)

            val userInputFood: EditText = droot.findViewById(R.id.editTextFoodItem)
            val userInputType: EditText = droot.findViewById(R.id.editTextFoodType)
            val userInputQuantity: EditText = droot.findViewById(R.id.editTextQuantity)

            alertDialog2 = AlertDialog.Builder(context)
            alertDialog2.setView(droot)
            alertDialog2.setPositiveButton("Enter") { dialog, which ->

                val name = userInputFood.text.toString()
                val type = userInputType.text.toString()
                val quantity = userInputQuantity.text.toString()

                foodNames.add(name)
                foodItems.add(type)
                quanityList.add(quantity)

                Toast.makeText(context, "Successfully Entered an Item", Toast.LENGTH_SHORT).show()

            }
            alertDialog2.setNeutralButton("Cancel"){dialog, which ->
                dialog.cancel()
            }

            val alert = alertDialog2.create()
            alert.show()

            val button: Button = alert.getButton(AlertDialog.BUTTON_NEUTRAL)
            val buttonEnt: Button = alert.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setBackgroundColor(resources.getColor(R.color.red))
            buttonEnt.setBackgroundColor(resources.getColor(R.color.green))

            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(20,0,20,0);
            buttonEnt.setLayoutParams(params)

        }

        removeButton.setOnClickListener {
            foodNames.clear()
            foodItems.clear()
            quanityList.clear()

            mRecyclerView.adapter = context?.let { it1 -> MyRecyclerViewAdapter(foodNames, foodItems, quanityList ,R.layout.row_layout, it1) }
        }

        mRecyclerView.adapter = context?.let { MyRecyclerViewAdapter(foodNames, foodItems, quanityList ,R.layout.row_layout, it) }

        return root
    }

}