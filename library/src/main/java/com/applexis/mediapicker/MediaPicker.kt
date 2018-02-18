package com.applexis.mediapicker

import android.app.Activity
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import java.io.File

/**
 * @author applexis
 */
class MediaPicker(private val activity: Activity) {

    companion object {
        val CAMERA_PHOTO = 0
        val GALLERY_PHOTO = 1
        val CAMERA_VIDEO = 2
        val GALLERY_VIDEO = 3

        private val CODE_ADD = 1000
        private val CAMERA_IMAGE_CAPTURE = CAMERA_PHOTO + CODE_ADD
        private val LIBRARY_PHOTO_REQUEST = GALLERY_PHOTO + CODE_ADD
        private val CAMERA_VIDEO_CAPTURE = CAMERA_VIDEO + CODE_ADD
        private val LIBRARY_VIDEO_REQUEST = GALLERY_VIDEO + CODE_ADD

        private var tmpDestinationPath = ""
    }

    private val OUTPUT_FILENAME_DIR = File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_MOVIES)
    private lateinit var callback: (source: Int, path: String) -> Unit
    var dialog: BaseMPDialog? = null
        set(value) {
            field = value
            dialog?.init(this)
        }

    fun show(callback: (source: Int, path: String) -> Unit) {
        this.callback = callback
        (dialog ?: throw RuntimeException("Dialog is not set")).show()
    }

    fun dismiss() {
        (dialog ?: throw RuntimeException("Dialog is not set")).dismiss()
    }

    internal fun pickPhotoFromCamera() {
        val name = currentDatetimeFilename()
        if (!OUTPUT_FILENAME_DIR.exists()) {
            OUTPUT_FILENAME_DIR.mkdirs()
        }
        val destination = File(
                Environment.getExternalStorageDirectory(),
                Environment.DIRECTORY_MOVIES + "/" + name + ".jpg"
        )

        tmpDestinationPath = destination.absolutePath

        val uriPhoto = FileProvider.getUriForFile(activity,
                activity.packageName + ".provider", destination)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto)
        dismiss()
        activity.startActivityForResult(intent, CAMERA_IMAGE_CAPTURE)
    }

    internal fun pickVideoFromCamera() {
        val nameVideo = currentDatetimeFilename()
        if (!OUTPUT_FILENAME_DIR.exists()) {
            OUTPUT_FILENAME_DIR.mkdirs()
        }
        val destination = File(
                Environment.getExternalStorageDirectory(),
                Environment.DIRECTORY_MOVIES + "/" + nameVideo + ".mp4"
        )

        tmpDestinationPath = destination.absolutePath

        val uriVideo = FileProvider.getUriForFile(activity,
                activity.packageName + ".provider", destination)
        val intentVideo = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intentVideo.putExtra(MediaStore.EXTRA_OUTPUT, uriVideo)
        dismiss()
        activity.startActivityForResult(intentVideo, CAMERA_VIDEO_CAPTURE)
    }

    internal fun pickPhotoFromGallery() {
        val selectIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectIntent.type = "image/*"
        dismiss()
        activity.startActivityForResult(selectIntent, LIBRARY_PHOTO_REQUEST)
    }

    internal fun pickVideoFromGallery() {
        val selectIntentVid = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        selectIntentVid.type = "video/*"
        dismiss()
        activity.startActivityForResult(selectIntentVid, LIBRARY_VIDEO_REQUEST)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                LIBRARY_PHOTO_REQUEST, LIBRARY_VIDEO_REQUEST -> {
                    val selectedImageUri = data?.data
                    try {
                        val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)
                        val cursor = activity.contentResolver.query(selectedImageUri, filePathColumn, null, null, null)
                        cursor!!.moveToFirst()

                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val resultPath = cursor.getString(columnIndex)
                        cursor.close()
                        callback(requestCode - CODE_ADD, resultPath)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                CAMERA_IMAGE_CAPTURE, CAMERA_VIDEO_CAPTURE -> {
                    callback(requestCode - CODE_ADD, tmpDestinationPath)
                }
            }
        }
    }

}