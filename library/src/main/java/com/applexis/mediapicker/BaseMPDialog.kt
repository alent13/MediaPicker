package com.applexis.mediapicker

abstract class BaseMPDialog {

    private lateinit var mediaPicker: MediaPicker

    fun init(mediaPicker: MediaPicker) {
        this.mediaPicker = mediaPicker
    }

    fun onPhotoCamera() {
        mediaPicker.pickPhotoFromCamera()
    }

    fun onVideoCamera() {
        mediaPicker.pickVideoFromCamera()
    }

    fun onPhotoGallery() {
        mediaPicker.pickPhotoFromGallery()
    }

    fun onVideoGallery() {
        mediaPicker.pickVideoFromGallery()
    }

    open fun show() {}

    open fun dismiss() {}

}