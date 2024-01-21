package com.ajrocks.conversationnotes.viewmodels

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ajrocks.conversationnotes.MainActivity
import com.ajrocks.conversationnotes.login.GmailLogin
import com.ajrocks.conversationnotes.login.GmailLoginSuccessNoArgsCallback
import com.ajrocks.conversationnotes.utils.LogoutTask

class AllConversationsPageVM: ViewModel() {

    enum class SORT_OPTIONS {
        SORT_ALL,
        SORT_MOST_RECENT
    }

    val currentSelectedOption = MutableLiveData(SORT_OPTIONS
        .SORT_ALL)

    fun logout(activity: ComponentActivity){
        val gmailLogin = GmailLogin(activity)
        gmailLogin.signOut(
            object: GmailLoginSuccessNoArgsCallback {
                override fun onSuccess() {
                    val intent = Intent(activity, MainActivity::class.java)
                    LogoutTask(activity.baseContext).complete()
                    activity.startActivity(intent)
                    activity.finish()
                }
            }
        )
    }

    fun onSortOptionSelected(sortOptionType: SORT_OPTIONS){
        currentSelectedOption.value = sortOptionType
    }

    fun indexToSortOption(index: Int): SORT_OPTIONS{
        return SORT_OPTIONS.values()[index]
    }

}