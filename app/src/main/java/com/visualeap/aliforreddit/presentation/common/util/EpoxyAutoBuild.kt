package com.visualeap.aliforreddit.presentation.common.util

import com.airbnb.epoxy.EpoxyController
import com.visualeap.aliforreddit.presentation.postDetail.PostDetailEpoxyController
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class EpoxyAutoBuild<T>(private val epoxyController: EpoxyController, defaultValue: T) :
    ReadWriteProperty<Any?, T> {
    private var value: T = defaultValue
    override fun getValue(thisRef: Any?, property: KProperty<*>) = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
        epoxyController.requestModelBuild()
    }
}