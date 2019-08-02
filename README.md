[![JitPack](https://jitpack.io/v/alent13/MediaPicker.svg)](https://jitpack.io/#alent13/MediaPicker)

# What is MediaPicker?

Easy way to select photo and video files on Android.
Library allows you to create dialog that can: take photo from camera, make video from camera, select image from gallery, select video from gallery.

## Demo

![Screenshot](https://github.com/alent13/MediaPicker/blob/master/Screenshot.jpg?raw=true?raw=true)

Basic dialog using MPDialog class.

## How to use

### Setup
Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency
```
	dependencies {
	        implementation 'com.github.alent13:MediaPicker:v0.21'
	}
```

### How to use
Create an instance of MediaPicker. Constructor needs an activity as argument.
```
    val mediaPicker: MediaPicker = MediaPicker(this)
```

Create and set dialog(otherwise you'll get an exception). In example we use `MPDialog` class that provides builder with `add` function to add pick variant.
```
    mediaPicker.dialog = MPDialog.Builder(this)
          .add(MediaPicker.CAMERA_PHOTO, "Custom photo select text")
          .add(MediaPicker.CAMERA_VIDEO)
          .add(MediaPicker.GALLERY_PHOTO)
          .add(MediaPicker.GALLERY_VIDEO, "Custom gallery video select text")
          .build()
```

You need to grand the permissions(`Manifest.permission.READ_EXTERNAL_STORAGE`, `Manifest.permission.WRITE_EXTERNAL_STORAGE`, `Manifest.permission.CAMERA`, `Manifest.permission.RECORD_AUDIO`).

After permission granted call show function with callback. `source` indicate users choise (`MediaPicker.CAMERA_PHOTO`, `MediaPicker.GALLERY_PHOTO`, `MediaPicker.CAMERA_VIDEO`, `MediaPicker.GALLERY_VIDEO`). `path` contains the result of choise.
```
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
```

MediaPicker needs to parse onActivityResult() data to send callback. So create `onActivityResult()` function that will call `handleActivityResult()`.
```
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mediaPicker.handleActivityResult(requestCode, resultCode, data)
    }
```

## Customization

Basic example shows use of MPDialog - basic AlertDialog with customizing buttons setup.
You also can make your own dialog, all you need create a class inherited from BaseMPDialog, provides `show()` and `dismiss()` functions and call `onPhotoCamera()`, `onVideoCamera()`, `onPhotoGallery()`, `onVideoGallery()` functions to start the corresponding function.
