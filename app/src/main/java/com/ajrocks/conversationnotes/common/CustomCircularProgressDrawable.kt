package com.ajrocks.conversationnotes.common

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.color.MaterialColors

class CustomCircularProgressDrawable(context: Context): CircularProgressDrawable(context) {
    init {
        setStyle(DEFAULT)
        centerRadius = 12f
        setColorSchemeColors(
            MaterialColors.getColor(
                context,
                android.R.attr.colorPrimary,
                "#000"
            )
        )
        start()
    }

}