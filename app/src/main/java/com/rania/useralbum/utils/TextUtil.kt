package com.rania.useralbum.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan

object TextUtil {

    //coloring text between startIndex and endText with specific color textColor
    fun colorText(
        textToColor: String,
        startIndex: Int,
        endIndex: Int,
        textColor: Int
    ): Spannable {
        val coloredText: Spannable = SpannableString(textToColor)
        coloredText.setSpan(
            ForegroundColorSpan(textColor), startIndex, endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return coloredText
    }
}