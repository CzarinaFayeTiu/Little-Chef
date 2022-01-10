package com.mobdeve.s12.avila.tiu.littlechef

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mobdeve.s12.avila.tiu.littlechef.DBHelper.MyDbHelper
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityMainBinding
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityRecipeMainBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_recipe_main.*

class RecipeMainActivity : DrawerBaseActivity() {

    //permission constants
    private val CAMERA_REQUEST_CODE = 100
    private val STORAGE_REQUEST_CODE = 101
    //image pick constants
    private val IMAGE_PICK_CAMERA_CODE = 102
    private val IMAGE_PICK_GALLERY_CODE = 103
    //arrays of permissions
    private lateinit var cameraPermissions:Array<String> //camera and storage
    private lateinit var storagePermissions:Array<String> //camera and storage
    //variables that will contain data to save in database
    private var imageUri: Uri? = null
    private var name:String? = ""
    private var category:String? = ""
    private var ingredients:String? = ""
    private var instructions:String? = ""
    private var link:String? = ""

    //action bar
    private var actionBar: ActionBar? = null

    lateinit var dbHelper:MyDbHelper

    //lateinit var toggle: ActionBarDrawerToggle
    var binding: ActivityRecipeMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityRecipeMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

/*
        actionBar = supportActionBar
        //title of actionbar
        actionBar!!.title = "Add Record"
        //back button in actionbar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)*/

        //init db helper
        dbHelper = MyDbHelper(this)

        //init permissions arrays
        cameraPermissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        storagePermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        //click imageview to pick image
        binding!!.ivProfile.setOnClickListener{
            //show image pick dialog
            imagePickDialog()

        }
        //click btnAdd to save record
        binding!!.btnAdd.setOnClickListener{
            inputData()
        }


    }


    private fun inputData() {
        //get data
        name = "" + edName.text.toString().trim()
        category = "" + edCategory.text.toString().trim()
        ingredients = "" + edIngredients.text.toString().trim()
        instructions = "" + edInstructions.text.toString().trim()
        link = "" + edLink.text.toString().trim()

        //save data to db
        val timestamp = System.currentTimeMillis()
        val id = dbHelper.insertRecord(
            ""+name,
            ""+category,
            ""+ingredients,
            ""+instructions,
            ""+imageUri,
            ""+link,
            ""+timestamp,
            ""+timestamp
        )
        Toast.makeText(this,"Record Added against ID $id", Toast.LENGTH_SHORT).show()
    }




    private fun imagePickDialog() {
        //options to display in dialog
        val options = arrayOf("Camera", "Gallery")
        //dialog
        var builder = AlertDialog.Builder(this)
        //title
        builder.setTitle("Pick Image From")
        //set items/options
        builder.setItems(options) { dialog, which ->
            //handle item clicks
            if (which==0) {
                //camera clicked
                if (!checkCameraPermissions()) {
                    requestCameraPermission()
                } else {
                    //permission already granted
                    pickFromCamera()
                }
            } else {
                //camera clicked
                if (!checkStoragePermissions()) {
                    requestStoragePermission()
                } else {
                    //permission already granted
                    pickFromGallery()
                }
            }
        }
        //show dialog
        builder.show()
    }

    private fun pickFromGallery() {
        //pick image from gallery using intent
        var galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*" //only image to be picked
        startActivityForResult(
            galleryIntent,
            IMAGE_PICK_GALLERY_CODE
        )
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermissions, STORAGE_REQUEST_CODE)
    }

    private fun checkStoragePermissions(): Boolean {
        //check if storage permission is enabled or not
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun pickFromCamera() {
        //pick image from camera
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Image Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image Description")
        //put image uri
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //intent to open camera
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(
            cameraIntent,
            IMAGE_PICK_CAMERA_CODE
        )
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermissions, CAMERA_REQUEST_CODE)
    }

    private fun checkCameraPermissions() : Boolean {
        //check if camera permissions (camera and storage) are enabled or not
        val results = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val results1 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        return results && results1
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() //go back to previous activity
        return super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE->{
                if(grantResults.isNotEmpty()){
                    //if allowed returns true otherwise false
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera()
                    } else {
                        Toast.makeText(this, "Camera and Storage Permissions are required", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            STORAGE_REQUEST_CODE-> {
                if(grantResults.isNotEmpty()){
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted) {
                        pickFromCamera()
                    } else {
                        Toast.makeText(this, "Storage Permissions are required", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (data != null){
                if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                    imageUri = data.data
                }

                binding!!.ivProfile.setImageURI(imageUri)

            }else{
                binding!!.ivProfile.setImageURI(imageUri)
            }
        }
    }

}