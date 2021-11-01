package com.example.cmsc434androidapplication.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to the Fridge Plus App"
    }
    val text: LiveData<String> = _text
}