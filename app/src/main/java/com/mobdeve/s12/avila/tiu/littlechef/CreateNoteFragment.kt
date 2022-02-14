package com.mobdeve.s12.avila.tiu.littlechef

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.facebook.CallbackManager
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*

/*
    base fragment is for coroutines and easy permission is for uploading
    images in notes from phone album
 */
class CreateNoteFragment : BaseFragment(),EasyPermissions.PermissionCallbacks,EasyPermissions.RationaleCallbacks{

    var selectedColor = "#F8FF97"
    var currentDate:String? = null
    private var READ_STORAGE_PERM = 123
    private var REQUEST_CODE_IMAGE = 456
    private var selectedImagePath = ""
    private var webLink = ""
    private var noteId = -1
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //find if note has been created or is new on creation
        noteId = requireArguments().getInt("noteId",-1)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    //set view status whether visible or gone
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (noteId != -1){ //note has been created

            launch {
                context?.let {
                    var notes = NotesDatabase.getDatabase(it).noteDao().getSpecificNote(noteId)
                    colorView.setBackgroundColor(Color.parseColor(notes.color))
                    selectedColor = notes.color.toString()
                    etNoteTitle.setText(notes.title)
                    etNoteSubTitle.setText(notes.subTitle)
                    etNoteDesc.setText(notes.noteText)
                    if (notes.imgPath != ""){
                        selectedImagePath = notes.imgPath!!
                        imgNote.setImageBitmap(BitmapFactory.decodeFile(notes.imgPath))
                        layoutImage.visibility = View.VISIBLE
                        imgNote.visibility = View.VISIBLE
                        imgDelete.visibility = View.VISIBLE
                    }else{
                        layoutImage.visibility = View.GONE
                        imgNote.visibility = View.GONE
                        imgDelete.visibility = View.GONE
                    }

                    if (notes.webLink != ""){
                        webLink = notes.webLink!!
                        tvWebLink.text = notes.webLink
                        layoutWebUrl.visibility = View.VISIBLE
                        etWebLink.setText(notes.webLink)
                        imgUrlDelete.visibility = View.VISIBLE
                    }else{
                        imgUrlDelete.visibility = View.GONE
                        layoutWebUrl.visibility = View.GONE
                    }
                }
            }
        }

        //calls the broadcast receiver below
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            BroadcastReceiver, IntentFilter("bottom_sheet_action")
        )

        //date of create note
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

        currentDate = sdf.format(Date())
        colorView.setBackgroundColor(Color.parseColor("#F8FF97"))

        tvDateTime.text = currentDate

        imgDone.setOnClickListener {
            if (noteId != -1){
                //if note has been created then just update it
                updateNote()
            }else{
                //if note has not been created yet save note
                saveNote()
            }
        }

        //if user opens a note then preses the back img
        imgBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        imgMore.setOnClickListener{
            //when the line at the bottom of create note is pressed load the bottom fragment
            var noteBottomSheetFragment = NotesBottomSheetFragment.newInstance(noteId)
            noteBottomSheetFragment.show(requireActivity().supportFragmentManager,"Note Bottom Sheet Fragment")
        }

        //delete a note
        imgDelete.setOnClickListener {
            selectedImagePath = ""
            layoutImage.visibility = View.GONE
        }

        //ok button of url input - optional
        btnOk.setOnClickListener {
            if (etWebLink.text.toString().trim().isNotEmpty()){
                checkWebUrl()
            }else{
                Toast.makeText(requireContext(),"Url is Required",Toast.LENGTH_SHORT).show()
            }
        }

        //cancel button of urlinput if user doesnt want
        btnCancel.setOnClickListener {
            if (noteId != -1){
                tvWebLink.visibility = View.VISIBLE
                layoutWebUrl.visibility = View.GONE
            }else{
                layoutWebUrl.visibility = View.GONE
            }

        }
        // x image at the upper right corner of url box to delete
        imgUrlDelete.setOnClickListener {
            webLink = ""
            tvWebLink.visibility = View.GONE
            imgUrlDelete.visibility = View.GONE
            layoutWebUrl.visibility = View.GONE
        }

        //if the url link the  user input is clicked then go to that website
        tvWebLink.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW,Uri.parse(etWebLink.text.toString()))
            startActivity(intent)
        }

        //share to facebook
        imgShare.setOnClickListener {
            shareNote()
        }

    }

    //update changes in note
    private fun updateNote(){
        launch {

            context?.let {
                var notes = NotesDatabase.getDatabase(it).noteDao().getSpecificNote(noteId)
                //load everything into note object again
                notes.title = etNoteTitle.text.toString()
                notes.subTitle = etNoteSubTitle.text.toString()
                notes.noteText = etNoteDesc.text.toString()
                notes.dateTime = currentDate
                notes.color = selectedColor
                notes.imgPath = selectedImagePath
                notes.webLink = webLink
                //call for update note to update contents in db
                NotesDatabase.getDatabase(it).noteDao().updateNote(notes)
                etNoteTitle.setText("")
                etNoteSubTitle.setText("")
                etNoteDesc.setText("")
                layoutImage.visibility = View.GONE
                imgNote.visibility = View.GONE
                tvWebLink.visibility = View.GONE
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    //save it in room database
    private fun saveNote(){

        if (etNoteTitle.text.isNullOrEmpty()){
            Toast.makeText(context,"Note Title is Required",Toast.LENGTH_SHORT).show()
        }
        else if (etNoteSubTitle.text.isNullOrEmpty()){

            Toast.makeText(context,"Note Sub Title is Required",Toast.LENGTH_SHORT).show()
        }

        else if (etNoteDesc.text.isNullOrEmpty()){

            Toast.makeText(context,"Note Description is Required",Toast.LENGTH_SHORT).show()
        }

        else{
            /*
                coroutines help in accomplishing background task with lightweight thread
                without having to call callback
                use launch to complete that task and then do something on Main Thread
             */
            launch {
                //assign user inputs to a note object
                var notes = Notes()
                notes.title = etNoteTitle.text.toString()
                notes.subTitle = etNoteSubTitle.text.toString()
                notes.noteText = etNoteDesc.text.toString()
                notes.dateTime = currentDate

                if(selectedColor.isNullOrEmpty()){
                    selectedColor = "#F8FF97"
                }else {
                    notes.color = selectedColor
                }
                notes.imgPath = selectedImagePath
                notes.webLink = webLink
                context?.let {
                    //save note object to room db in notes database
                    NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                    etNoteTitle.setText("")
                    etNoteSubTitle.setText("")
                    etNoteDesc.setText("")
                    layoutImage.visibility = View.GONE
                    imgNote.visibility = View.GONE
                    tvWebLink.visibility = View.GONE
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }

    }
    //share to facebook
    private fun shareNote(){

            launch {
                var notes = Notes()
                var title= etNoteTitle.text.toString()
                var subTitle = etNoteSubTitle.text.toString()
                var noteText = etNoteDesc.text.toString()
                var dateTime = currentDate
                var imgPath = selectedImagePath
                var webLink = webLink

                var quote = "Recipe Title: $title \nSub Title: $subTitle\nIngredients and Instructions:\n $noteText \n\nCreated on: $dateTime"

                Log.d("FACEBOOK", "THIS IS THE VAR QUOTE \n$quote")
                Log.d("FACEBOOK", "THIS IS THE IMGPATH: $imgPath")
                Log.d("FACEBOOK", "THIS IS THE webLink: $webLink")

                callbackManager = CallbackManager.Factory.create()

                //pronounce publicity of little chef by adding a hastag in the template post to be shared
                var hashtag = ShareHashtag.Builder()
                    .setHashtag("#LittleChef")
                    .build()

                //share a url link to share the note contents in quotes, <- restriction of facebook
                var shareLinkContent:ShareLinkContent

                //if the user provides a web link then share that link
                if(webLink.trim().isNotEmpty()) {
                    shareLinkContent = ShareLinkContent.Builder()
                        .setQuote(quote)
                        .setShareHashtag(hashtag)
                        .setContentUrl(Uri.parse("$webLink"))
                        .build()
                    Log.d("FACEBOOK", "THERE IS A WEB LINK")
                }else{
                    //if a user doesnt provide a web url use a default one
                    shareLinkContent = ShareLinkContent.Builder()
                        .setQuote(quote)
                        .setShareHashtag(hashtag)
                        .setContentUrl(Uri.parse("www.littlechef-mobdeve.com"))
                        .build()
                    Log.d("FACEBOOK", "THERE IS NO LINK")
                }

                //share this note with the contents written in shareLinkContent
                ShareDialog.show(this@CreateNoteFragment, shareLinkContent)

            }


    }
    //get the id of the current note opened and delete it
    private fun deleteNote(){

        launch {
            context?.let {
                NotesDatabase.getDatabase(it).noteDao().deleteSpecificNote(noteId)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }
    //check if url is valid
    private fun checkWebUrl(){
        if (Patterns.WEB_URL.matcher(etWebLink.text.toString()).matches()){
            layoutWebUrl.visibility = View.GONE
            etWebLink.isEnabled = false
            webLink = etWebLink.text.toString()
            tvWebLink.visibility = View.VISIBLE
            tvWebLink.text = etWebLink.text.toString()
        }else{
            Toast.makeText(requireContext(),"Url is not valid",Toast.LENGTH_SHORT).show()
        }
    }

    //connects to the action taken in notebottomsheetfragment
    //broadcast to receive the action
    private val BroadcastReceiver : BroadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {

            var actionColor = p1!!.getStringExtra("action")

            when(actionColor!!){

                "Blue" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }

                "Yellow" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Purple" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Green" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Orange" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Black" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }

                "Image" ->{
                    readStorageTask()
                    layoutWebUrl.visibility = View.GONE
                }

                "WebUrl" ->{
                    layoutWebUrl.visibility = View.VISIBLE
                }
                "DeleteNote" -> {
                    //delete note
                    deleteNote()
                }


                else -> {
                    layoutImage.visibility = View.GONE
                    imgNote.visibility = View.GONE
                    layoutWebUrl.visibility = View.GONE
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }
            }
        }

    }

    override fun onDestroy() {

        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(BroadcastReceiver)
        super.onDestroy()


    }

    //permission for accessing image in album
    private fun hasReadStoragePerm():Boolean{
        return EasyPermissions.hasPermissions(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    //either ask for permission or open gallery
    private fun readStorageTask(){
        if (hasReadStoragePerm()){
            pickImageFromGallery()
        }else{
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.storage_permission_text),
                READ_STORAGE_PERM,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    //open album and pic an image
    private fun pickImageFromGallery(){
        var intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(requireActivity().packageManager) != null){
            startActivityForResult(intent,REQUEST_CODE_IMAGE)
        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        var filePath:String? = null
        var cursor = requireActivity().contentResolver.query(contentUri,null,null,null,null)
        if (cursor == null){
            filePath = contentUri.path
        }else{
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK){
            if (data != null){
                var selectedImageUrl = data.data
                if (selectedImageUrl != null){
                    try {
                        var inputStream = requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        var bitmap = BitmapFactory.decodeStream(inputStream)
                        imgNote.setImageBitmap(bitmap)
                        imgNote.visibility = View.VISIBLE
                        layoutImage.visibility = View.VISIBLE

                        selectedImagePath = getPathFromUri(selectedImageUrl)!!
                    }catch (e:Exception){
                        Toast.makeText(requireContext(),e.message,Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,requireActivity())
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(),perms)){
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onRationaleDenied(requestCode: Int) {

    }

    override fun onRationaleAccepted(requestCode: Int) {

    }

}

