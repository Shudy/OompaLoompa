package com.eliasortiz.oompaloomparrhh.utils

import android.content.Context
import android.util.TypedValue
import java.util.*

fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).toInt()

fun String.capitalizeFirstLetter(): String {
    return if (this.isNotEmpty()) {
        this.substring(0, 1).uppercase(Locale.getDefault()) + this.substring(1)
            .lowercase(Locale.getDefault())
    } else {
        this
    }
}