package com.applexis.mediapicker

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_sample.*
import java.util.ArrayList

class SampleActivity : AppCompatActivity() {

    // Create an instance of MediaPicker
    val mediaPicker: MediaPicker = MediaPicker(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        // You need to set dialog, otherwise you'll get an exception
        mediaPicker.dialog = MPDialog.Builder(this)
                .add(MediaPicker.CAMERA_PHOTO, "Custom photo select text")
                .add(MediaPicker.CAMERA_VIDEO)
                .add(MediaPicker.GALLERY_PHOTO)
                .add(MediaPicker.GALLERY_VIDEO, "Custom gallery video select text")
                .build()

        picker.setOnClickListener {
            // You need to grand the permissions
            TedPermission.with(this)
                    .setPermissionListener(object : PermissionListener {
                        override fun onPermissionGranted() {
                            // After permission granted call show function with callback
                            mediaPicker.show { source, path ->
                                mediaType.text = when (source) {
                                    MediaPicker.CAMERA_PHOTO -> "Photo from camera"
                                    MediaPicker.GALLERY_PHOTO -> "Photo from gallery"
                                    MediaPicker.CAMERA_VIDEO -> "Video from camera"
                                    MediaPicker.GALLERY_VIDEO -> "Video from gallery"
                                    else -> "other"
                                }
                                mediaPath.text = path
                            }
                        }

                        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                            Toast.makeText(this@SampleActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
                        }
                    })
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO)
                    .check()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // MediaPicker needs to parse onActivityResult() data to send callback
        mediaPicker.handleActivityResult(requestCode, resultCode, data)
    }
}
