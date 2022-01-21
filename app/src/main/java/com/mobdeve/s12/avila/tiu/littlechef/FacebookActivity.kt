package com.mobdeve.s12.avila.tiu.littlechef

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.facebook.*
import com.facebook.R
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacebookBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        loginButton = binding!!.loginButton
        textView = binding!!.tvName
        imageView = binding!!.tvProfilePic

        callbackManager = CallbackManager.Factory.create()

        loginButton.setPermissions(listOf("email"))

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

    }

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

                    val pic = `object`.getJSONObject("picture")
                        .getJSONObject("data").getString("url")
                    Picasso.get().load(pic).into(imageView)
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
                imageView!!.setImageResource(0)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        accessTokenTracker.stopTracking()
    }

}