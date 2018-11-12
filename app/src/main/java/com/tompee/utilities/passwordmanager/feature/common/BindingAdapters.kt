@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package com.tompee.utilities.passwordmanager.feature.common

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion

@BindingAdapter("visibleGone")
fun showHide(view: View, isVisible: java.lang.Boolean?) {
    view.visibility = if (isVisible != null && isVisible.booleanValue()) View.VISIBLE else View.GONE
}

@BindingConversion
fun listToVisibility(list: List<*>?): Int {
    return if (list == null || list.isEmpty()) View.INVISIBLE else View.VISIBLE
}