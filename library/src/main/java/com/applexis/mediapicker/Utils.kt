package com.applexis.mediapicker

import java.text.SimpleDateFormat
import java.util.*

fun currentDatetimeFilename(): String =
        SimpleDateFormat("ddMMyyyyhhmmss", Locale.ENGLISH).format(Date())