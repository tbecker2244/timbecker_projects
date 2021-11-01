package com.example.semester_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception


// The activity to show Discovery Page
class MainActivity : Activity() {
    lateinit var toolBar: Toolbar
    lateinit var recycleView: RecyclerView
    lateinit var mAdapter: CardAdapter
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ToolBar and Menu
        toolBar = findViewById(R.id.toolbar)
        toolBar.setTitle(R.string.app_name)
        toolBar.subtitle = "Discovery"
        toolBar.inflateMenu(R.menu.switch_activities)
        toolBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.map -> {
                    val intent = Intent(this, MapActivity::class.java)
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

        mAuth = FirebaseAuth.getInstance()

        // CardView & RecycleView
        recycleView = findViewById(R.id.recyclerView)
        recycleView.layoutManager = LinearLayoutManager(this)
        mAdapter = CardAdapter(this)
        recycleView.adapter = mAdapter
    }

    override fun onStart() {
        super.onStart()

        FirebaseDatabase.getInstance().getReference("tours")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mAdapter.clear()

                    var tour: Tour? = null
                    for (postSnapshot in dataSnapshot.children) {
                        try {
                            tour = postSnapshot.getValue(Tour::class.java)
                        } catch (e: Exception) {
                            Log.e(TAG, e.toString())
                        } finally {
                            mAdapter.add(tour!!, postSnapshot.key!!)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

        if (mAuth.currentUser == null) {
            mAdapter.clearFavorate()
            return
        }


        FirebaseDatabase.getInstance().getReference("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mAdapter.clearFavorate()

                    for (postSnapshot in dataSnapshot.child(mAuth.currentUser!!.uid)
                        .children) {
                        try {
                            mAdapter.add(postSnapshot.key!!)
                        } catch (e: Exception) {
                            Log.e(TAG, e.toString())
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })


    }

    companion object {
        private const val TAG = "Semester-Project"

    }

}


