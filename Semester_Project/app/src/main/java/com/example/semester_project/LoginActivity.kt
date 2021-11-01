package com.example.semester_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception


// The activity used to handle user log-in and user page after log-in
class LoginActivity: Activity() {
    private lateinit var toolBar:Toolbar
    private lateinit var loginBut:Button
    private lateinit var registerBut:Button
    private lateinit var clearBut:Button
    private lateinit var userNameView: EditText
    private lateinit var passwordView: EditText

    private lateinit var mAuth: FirebaseAuth

    private lateinit var mAdapter: FavorateListAdapter
    private lateinit var mName: TextView
    private lateinit var mFavorateList: ListView
    private lateinit var user: DatabaseReference
    private lateinit var addTour: Button
    private lateinit var signOut: Button


    // Check if login
    // if login, set the user page layout
    // if not, set the login layout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        Log.i(TAG, (mAuth.currentUser == null).toString())
        if (mAuth.currentUser == null) {
            setContentView(R.layout.login_page)

            setToolbar()

            loginBut = findViewById(R.id.loginBtn)
            clearBut = findViewById(R.id.clearBtn)
            registerBut = findViewById(R.id.registerBtn)
            userNameView = findViewById(R.id.userName)
            passwordView = findViewById(R.id.password)

            loginBut.setOnClickListener{ loginUserAccount() }

            clearBut.setOnClickListener{
                userNameView.setText("")
                passwordView.setText("")
            }

            registerBut.setOnClickListener{
                val intent = Intent(this, RegisterActivity::class.java)
                startActivityForResult(intent, REGISTER_REQUEST)
            }

        } else {
            setContentView(R.layout.user_page)
            setToolbar()

            addTour = findViewById(R.id.editBut)
            addTour.setOnClickListener {
                val tmpIntent = Intent(this, EditActivity::class.java)
                startActivity(tmpIntent)
            }

            mName = findViewById(R.id.userNameText)
            val regex = "@".toRegex()
            mName.text = regex.split(mAuth.currentUser!!.email!!)[0]

            mAdapter = FavorateListAdapter(applicationContext)
            mFavorateList = findViewById(R.id.favorateListView)
            mFavorateList.adapter = mAdapter


            mFavorateList.onItemClickListener = AdapterView.OnItemClickListener{ _, _, i, _ ->
                val tour = mAdapter.getItem(i)
                val tmpIntent = Intent(this, TourDetailActivity::class.java)
                tour.packageIntent(tmpIntent)
                tmpIntent.putExtra(TOUR_ID, mAdapter.getTourId(i))
                startActivity(tmpIntent)
            }

            signOut = findViewById(R.id.signOutBut)
            signOut.setOnClickListener {
                mAuth.signOut()
                recreate()
            }

            user = FirebaseDatabase.getInstance().getReference("users")

            user.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mAdapter.clear()

                    var tour: Tour? = null
                    for (postSnapshot in dataSnapshot.child(mAuth.currentUser!!.uid)
                        .children) {
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
        }
    }

    private fun setToolbar() {
        // ToolBar and Menu
        toolBar = findViewById(R.id.toolbar)
        toolBar.setTitle(R.string.app_name)
        toolBar.subtitle = "User"
        toolBar.inflateMenu(R.menu.switch_activities)
        toolBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.map -> {
                    val intent = Intent(this, MapActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.discovery -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
    }

    // success registration would automatically log in
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REGISTER_REQUEST && resultCode == RESULT_OK) {
            recreate()
        }
    }

    private fun loginUserAccount() {

        val email = userNameView.text.toString()
        val password = passwordView.text.toString()
        val validator = Validators()

        if (!validator.validEmail(email)) {
            Toast.makeText(applicationContext, "Please enter a valid email...",
                Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Login successful!",
                        Toast.LENGTH_LONG).show()
                    recreate()
                } else {
                    Log.i(TAG,"why")
                    Toast.makeText(applicationContext, "Login failed! Please try again later",
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    companion object {

        private const val REGISTER_REQUEST = 0
        private const val TOUR_ID = "TOUR_ID"

        private const val TAG = "Semester-Project"

    }
}