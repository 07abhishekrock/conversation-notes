package com.ajrocks.conversationnotes.login

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

interface GmailLoginSuccessCallback {
   fun onSuccess(id: String, email: String, name: String): Unit
}

interface GmailLoginSuccessNoArgsCallback {
    fun onSuccess(): Unit
}

interface GmailLoginErrorCallback {
    fun onError(errorReason: String): Unit
}

class GmailLogin(activity: ComponentActivity) {

    val callingActivity = activity
    private lateinit var invokingIntent: Intent
    private var googleSignInClient: GoogleSignInClient? = null
    private lateinit var googleSignInActivity: ActivityResultLauncher<Intent>

    private fun initialize(){
        val gs = GoogleSignInOptions.Builder().requestEmail().requestId().build()
        googleSignInClient = GoogleSignIn.getClient(callingActivity, gs)
    }


    fun setupSignIn(
        callback: GmailLoginSuccessCallback,
        errorCallback: GmailLoginErrorCallback
    ){

        if(googleSignInClient == null) {
            initialize()
        }

        invokingIntent = googleSignInClient!!.signInIntent

        googleSignInActivity = callingActivity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if(it.resultCode == RESULT_OK && it.data != null) {
                val loginTask = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                val accountDetails = loginTask.result
                if(accountDetails.id?.isNotEmpty() == true) {
                    callback.onSuccess(
                        accountDetails.id!!,
                        accountDetails.email ?: "",
                        accountDetails.displayName ?: ""
                    )
                }
                else {
                    errorCallback.onError("No ID found for the User. Invalid User.")
                }
            }
            else {
                errorCallback.onError("Something went wrong.")
            }
        }
    }

    fun signIn(){
        googleSignInActivity.launch(invokingIntent)
    }

    fun signOut(
        callback: GmailLoginSuccessNoArgsCallback,
        errorCallback: GmailLoginErrorCallback? = null
    ){
        if(googleSignInClient == null){
            initialize()
        }

        googleSignInClient!!.signOut().addOnCompleteListener {
            if(it.isSuccessful){
                callback.onSuccess()
            }
            else{
                errorCallback?.onError("Could not sign out user")
            }
        }
    }



}