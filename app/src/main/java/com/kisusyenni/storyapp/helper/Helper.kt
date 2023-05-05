package com.kisusyenni.storyapp.helper
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun formatDate(dateString: String): String {
    val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val locale = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)
    val format = SimpleDateFormat(pattern, locale)
    val date = format.parse(dateString) as Date
    return DateFormat.getDateInstance(DateFormat.FULL).format(date)
}