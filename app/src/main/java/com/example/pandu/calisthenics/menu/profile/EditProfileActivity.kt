package com.example.pandu.calisthenics.menu.profile

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.db.database
import com.example.pandu.calisthenics.model.User
import kotlinx.android.synthetic.main.activity_edit_profile.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import okhttp3.MultipartBody
import android.graphics.Bitmap
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.content.Intent
import okhttp3.RequestBody
import android.provider.MediaStore
import android.provider.DocumentsContract
import android.app.Activity
import android.widget.Toast
import com.example.pandu.calisthenics.api.APIClient
import com.example.pandu.calisthenics.utils.after
import okhttp3.MediaType
import org.jetbrains.anko.alert
import org.jetbrains.anko.indeterminateProgressDialog
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
class EditProfileActivity : AppCompatActivity(){

    private var toolbarAct: Toolbar? = null
    private var menuItem: Menu? = null
    private val IMG_REQUEST = 777
    private var bitmap: Bitmap? = null
    private var photoProfile: MultipartBody.Part? = null
    companion object {
        val MY_PERMISSIONS_REQUEST_STORAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        toolbarAct = toolbar_profile
        setSupportActionBar(toolbar_profile)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        loadDataProfile()

        profile_image_edit.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkStoragePermission()) {
                    selectImage()
                }
            } else {
                selectImage()
            }
        }

    }


    private fun checkStoragePermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_STORAGE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_STORAGE
                )
            }
            return false
        } else {
            return true
        }

    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMG_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === IMG_REQUEST && resultCode === Activity.RESULT_OK && data != null) {
            val selectedImage = data.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                profile_image_edit.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val wholeID = DocumentsContract.getDocumentId(selectedImage)

            // Split at colon, use second item in the array
            val id = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

            val column = arrayOf(MediaStore.Images.Media.DATA)

            // where id is equal to
            val sel = MediaStore.Images.Media._ID + "=?"

            val cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, arrayOf(id), null
            )

            var filePath = ""

            val columnIndex = cursor!!.getColumnIndex(column[0])

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex)
            }
            cursor.close()
            val file = File(filePath)

            val reqFile = RequestBody.create(MediaType.parse("image/*"), file)

            photoProfile = MultipartBody.Part.createFormData("image", file.name, reqFile)

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        selectImage()
                    }

                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun upload() {
        val name = RequestBody.create(okhttp3.MultipartBody.FORM, et_name_edit_profile.text.toString())
        val weight = RequestBody.create(okhttp3.MultipartBody.FORM, et_weight_edit_profile.text.toString())
        val height = RequestBody.create(okhttp3.MultipartBody.FORM, et_height_edit_profile.text.toString())

        APIClient.getService(this)
            .editProfile(name, weight, height, photoProfile)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: retrofit2.Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
//                        Toast.makeText(this@EditProfileActivity, "Success update your profile", Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(this@EditProfileActivity, "Failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<User>, t: Throwable) {
                    Toast.makeText(this@EditProfileActivity, "Error: $t", Toast.LENGTH_SHORT).show()
                }
            })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        menuItem = menu

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.create_task -> {
                upload()
                indeterminateProgressDialog("Please wait a bit…").show()
                after(2000) {
                    indeterminateProgressDialog("Please wait a bit…").dismiss()
                    finish()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadDataProfile() {
        database.use {
            val result = select(User.TABLE_USER)
            val taskSQLite = result.parseSingle(classParser<User>())

            et_name_edit_profile.setText(taskSQLite.name)
            et_email_edit_profile.setText(taskSQLite.email)
            et_weight_edit_profile.setText(taskSQLite.weight)
            et_height_edit_profile.setText(taskSQLite.height)
            Glide.with(this@EditProfileActivity).load(taskSQLite.photo_profile).into(profile_image_edit)
        }
    }


}
