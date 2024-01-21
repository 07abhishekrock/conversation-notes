package com.ajrocks.conversationnotes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.ajrocks.conversationnotes.common.CustomCircularProgressDrawable
import com.ajrocks.conversationnotes.databinding.LoginActivityBinding
import com.ajrocks.conversationnotes.login.GmailLogin
import com.ajrocks.conversationnotes.login.GmailLoginErrorCallback
import com.ajrocks.conversationnotes.login.GmailLoginSuccessCallback
import com.ajrocks.conversationnotes.user.UserDetails


class MainActivity : ComponentActivity() {

    private lateinit var loginActivityBinding: LoginActivityBinding
    private lateinit var gmailLogin: GmailLogin
    private lateinit var userDetailsPref: UserDetails

    companion object {
        const val TAG = "MainActivity"
    }

    private fun startLoadingSignInButton(){
        loginActivityBinding.googleSignIn.isEnabled = false
        loginActivityBinding.googleSignIn.icon = CustomCircularProgressDrawable(this)
    }

    private fun setLoadingButtonToInitialState(){
        loginActivityBinding.googleSignIn.isEnabled = true
        loginActivityBinding.googleSignIn.icon = ContextCompat.getDrawable(this, R.drawable.google)
        loginActivityBinding.googleSignIn.text = getText(R.string.sign_in_with_google)
    }

    private fun onGoogleSignInSuccess(id: String, email: String, name: String){
        Toast.makeText(this, "You logged in successfully", Toast.LENGTH_SHORT).show()
        userDetailsPref.setUserName(email)
        redirectToAllConversationsPage()
    }

    private fun redirectToAllConversationsPage(){
        val intent = Intent(this, AllConversationsPage::class.java)
        startActivity(intent)
        finish()
    }

    private fun redirectIfUserAlreadyExists(){
        val currentStoredUserName = userDetailsPref.getUserName()
        Log.d(TAG, "lastStoredUsername: $currentStoredUserName")
        if(currentStoredUserName?.isEmpty() == false){
            redirectToAllConversationsPage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        loginActivityBinding = LoginActivityBinding.inflate(LayoutInflater.from(this))
        userDetailsPref = UserDetails(this)
        redirectIfUserAlreadyExists()
        setContentView(loginActivityBinding.root)
        gmailLogin = GmailLogin(this)

        gmailLogin.setupSignIn( object: GmailLoginSuccessCallback {
            override fun onSuccess(id: String, email: String, name: String) {
                setLoadingButtonToInitialState()
                onGoogleSignInSuccess(id, email, name)
            }
        } , object: GmailLoginErrorCallback {
            override fun onError(errorReason: String) {
                Log.d(TAG, "error: $errorReason'")
                setLoadingButtonToInitialState()
            }
        })

        loginActivityBinding.googleSignIn.setOnClickListener {
            startLoadingSignInButton()
            gmailLogin.signIn()
        }
        super.onCreate(savedInstanceState)
    }

}