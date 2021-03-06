package com.jhernandes.upcomingmovies.commons

import android.view.View.GONE
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.jhernandes.datamodule.ServiceModule
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Locale

private var baseDateFormat = "yyyy-MM-dd"
private var readableFormat = "MM-dd-yyyy"

@BindingAdapter("thumbnailUrl")
fun ImageView.setThumbnail(url: String?) {
    url?.let { ServiceModule().getImageService(this.context).loadImageInto(this, it, true) }
}

@BindingAdapter("imageUrl")
fun ImageView.setImage(url: String?) {
    url?.let { ServiceModule().getImageService(this.context).loadImageInto(this, url) }
}

@BindingAdapter("readableDate")
fun TextView.setDateFormatted(date: String) {
    try{
        val tempDate = SimpleDateFormat(baseDateFormat, Locale.getDefault()).parse(date)
        this.text = SimpleDateFormat(readableFormat, Locale.getDefault()).format(tempDate)
    } catch (e : Exception) {
        this.visibility = GONE
    }
}