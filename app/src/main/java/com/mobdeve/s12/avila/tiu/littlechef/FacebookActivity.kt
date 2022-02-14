package com.mobdeve.s12.avila.tiu.littlechef

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.facebook.*
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityFacebookBinding
import com.squareup.picasso.Picasso
import org.json.JSONException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class FacebookActivity: DrawerBaseActivity()  {
    private var callbackManager: CallbackManager? = null
    private lateinit var loginButton: LoginButton
    private var imageView: ImageView? = null
    private var textView: TextView? = null
    var binding:ActivityFacebookBinding? =null
    var profilePic:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacebookBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        loginButton = binding!!.loginButton
        textView = binding!!.tvName
        imageView = binding!!.tvProfilePic

        /*
            The CallbackManager manages the callbacks into the FacebookSdk
            from an Activity's or Fragment's onActivityResult() method.
         */
        callbackManager = CallbackManager.Factory.create()

        //get permission from user to log into fb
        loginButton.setPermissions(listOf("email"))

        //login status
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Log.d("FACEBOOK", "Login Successful!")
            }

            override fun onCancel() {
                Log.d("FACEBOOK", "Login Cancelled")
            }

            override fun onError(error: FacebookException) {
                Log.d("FACEBOOK", "Login Error")
            }
        })
        //if user has already previously log in load picture and name into page
        val graphRequest =
            GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken())
            { `object`, response ->

                Log.d("FACEBOOK", `object`.toString())

                try {
                    val name = `object`!!.getString("name")
                    textView!!.text = name

                    profilePic = `object`.getJSONObject("picture")
                        .getJSONObject("data").getString("url")

                    Log.d("FACEBOOKACTIVITYPICTURE", "PIC URL STRING: $profilePic")

                    Picasso.get().load(profilePic).into(imageView)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

        val bundle = Bundle()
        bundle.putString(
            "fields",
            "name, first_name, last_name, email, picture"
        )
        graphRequest.parameters = bundle
        graphRequest.executeAsync()

    }

    //load image and picture upon successful log in
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        val graphRequest =
            GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken())
            { `object`, response ->

                Log.d("FACEBOOK", `object`.toString())

                try {
                    val name = `object`!!.getString("name")
                    textView!!.text = name

                    profilePic = `object`.getJSONObject("picture")
                        .getJSONObject("data").getString("url")

                    Log.d("FACEBOOKACTIVITYPICTURE", "PIC URL STRING: $profilePic")

                    Picasso.get().load(profilePic).into(imageView)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            }
        val bundle = Bundle()
        bundle.putString(
            "fields",
            "name, first_name, last_name, email, picture"
        )
        graphRequest.parameters = bundle
        graphRequest.executeAsync()
    }


    var accessTokenTracker: AccessTokenTracker = object : AccessTokenTracker() {
        override fun onCurrentAccessTokenChanged(
            oldAccessToken: AccessToken?,
            currentAccessToken: AccessToken?
        ) {
            if (currentAccessToken == null) { //if null it means log out
                LoginManager.getInstance().logOut()
                textView!!.text = ""
                imageView!!.setImageResource(R.drawable.person)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        accessTokenTracker.stopTracking()
    }

}