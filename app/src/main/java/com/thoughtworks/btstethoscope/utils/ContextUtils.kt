package com.thoughtworks.btstethoscope.utils

import android.app.Activity
import android.app.Dialog
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

inline fun <reified T : View> View.find(@IdRes id: Int): T = findViewById(id)
inline fun <reified T : View> Activity.find(@IdRes id: Int): T = findViewById(id)
@Deprecated(message = "Use support library fragments instead. Framework fragments were deprecated in API 28.")
inline fun <reified T : View> Fragment.find(@IdRes id: Int): T = view?.findViewById(id) as T

inline fun <reified T : View> Dialog.find(@IdRes id: Int): T = findViewById(id)

inline fun <reified T : View> View.findOptional(@IdRes id: Int): T? = findViewById(id) as? T
inline fun <reified T : View> Activity.findOptional(@IdRes id: Int): T? = findViewById(id) as? T
@Deprecated(message = "Use support library fragments instead. Framework fragments were deprecated in API 28.")
inline fun <reified T : View> Fragment.findOptional(@IdRes id: Int): T? =
    view?.findViewById(id) as? T

inline fun <reified T : View> Dialog.findOptional(@IdRes id: Int): T? = findViewById(id) as? T