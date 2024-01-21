package com.ajrocks.conversationnotes.utils

import android.content.Context
import com.ajrocks.conversationnotes.user.UserDetails

class LogoutTask(context: Context) {

    private val userDetails: UserDetails = UserDetails(context)

    fun complete(){
        userDetails.setUserName(null)
    }
}