package com.applexis.mediapicker

import android.app.AlertDialog
import android.content.Context

class MPDialog private constructor() : BaseMPDialog() {

    internal lateinit var dialog: AlertDialog

    override fun show() {
        dialog.show()
    }

    override fun dismiss() {
        dialog.dismiss()
    }

    class Builder(private var context: Context) {

        private val defaultLabels = arrayOf(
                "Take a photo",
                "Photo from gallery",
                "Make a video",
                "Video from gallery"
        )

        private var pickList = LinkedHashSet<PickVariant>()

        fun add(source: Int, label: String = defaultLabels[source]): MPDialog.Builder {
            pickList.add(PickVariant(source, label))
            return this
        }

        fun build(): MPDialog {
            val mpDialog = MPDialog()
            val builder = AlertDialog.Builder(context)
            builder.setItems(pickList.map { it.toString() }.toTypedArray(), { _, which ->
                when (pickList.elementAt(which).source) {
                    MediaPicker.CAMERA_PHOTO -> mpDialog.onPhotoCamera()
                    MediaPicker.CAMERA_VIDEO -> mpDialog.onVideoCamera()
                    MediaPicker.GALLERY_PHOTO -> mpDialog.onPhotoGallery()
                    MediaPicker.GALLERY_VIDEO -> mpDialog.onVideoGallery()
                }
            })
            mpDialog.dialog = builder.create()
            return mpDialog
        }

    }

    private class PickVariant(var source: Int, var label: String) {
        override fun toString(): String {
            return label
        }

        override fun equals(other: Any?): Boolean =
                when {
                    this === other -> true
                    other == null -> false
                    other !is PickVariant -> false
                    source != other.source -> false
                    else -> true
                }

        override fun hashCode(): Int = source * 31
    }

}