package com.ajrocks.conversationnotes.user

import android.content.Context

class UserDetails(_context: Context) {
    private var username: String? = null
    private var context: Context = _context

    init {
        initializeFromStore()
    }

    companion object {
        private val USERNAME_KEY = "username"
        private val STORE_KEY = "store"
    }

    private fun initializeFromStore(){
        setUserName(context.getSharedPreferences(STORE_KEY, Context.MODE_PRIVATE).getString
            (USERNAME_KEY, null))
    }

    fun setUserName(newUserName: String?) {
        username = newUserName
        val sharedPreferences = context.getSharedPreferences(STORE_KEY, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
           putString(USERNAME_KEY, newUserName)
           apply()
        }
    }

    fun getUserName(): String? {
        return username
    }
}