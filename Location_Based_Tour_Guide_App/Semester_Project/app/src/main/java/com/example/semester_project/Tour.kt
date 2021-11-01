package com.example.semester_project

import android.content.Intent


class Tour(val name: String = "", val description: String = "", val author: String = "") {

    fun packageIntent(intent: Intent) {
        intent.putExtra(NAME, name)
        intent.putExtra(AUTHOR, author)
        intent.putExtra(DESCRIPTION, description)
    }

    companion object {
        private const val AUTHOR = "AUTHOR"
        private const val DESCRIPTION = "DESCRIPTION"
        private const val NAME = "NAME"

    }
}