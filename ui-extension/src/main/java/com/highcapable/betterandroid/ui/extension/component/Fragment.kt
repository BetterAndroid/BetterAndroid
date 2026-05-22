/*
 * Better Android - Create more useful tool extensions for Android.
 * Copyright (C) 2019 HighCapable
 * https://github.com/BetterAndroid/BetterAndroid
 *
 * Apache License Version 2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file is created by fankes on 2022/11/24.
 */
@file:Suppress("unused", "FunctionName")
@file:JvmName("FragmentUtils")

package com.highcapable.betterandroid.ui.extension.component

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.LifecycleOwner
import com.highcapable.betterandroid.ui.extension.generated.BetterAndroidProperties
import com.highcapable.betterandroid.ui.extension.view.firstChildOrNull
import android.R as Android_R

/**
 * Get the [FragmentManager] from [FragmentActivity].
 * @receiver [FragmentActivity]
 * @return [FragmentManager]
 */
fun FragmentActivity.fragmentManager() = supportFragmentManager

/**
 * Get the [FragmentManager] from [Fragment].
 * @receiver [Fragment]
 * @param parent whether to get the parent [FragmentManager], default is false.
 * @return [FragmentManager]
 */
fun Fragment.fragmentManager(parent: Boolean = false) = if (parent) parentFragmentManager else childFragmentManager

/**
 * Get the parent [Fragment].
 * @receiver [Fragment]
 * @return [T] or null.
 */
inline fun <reified T : Fragment> Fragment.parentFragment() = parentFragment as? T?

/**
 * Find an exists [Fragment] by [id].
 * @see FragmentManager.findFragmentById
 * @receiver [FragmentManager]
 * @param id the [Fragment] ID to find.
 * @return [T] or null.
 */
inline fun <reified T : Fragment> FragmentManager.findFragment(@IdRes id: Int) = findFragmentById(id) as? T?

/**
 * Find an exists [Fragment] by [tag].
 * @see FragmentManager.findFragmentByTag
 * @receiver [FragmentManager]
 * @param tag the [Fragment] tag to find.
 * @return [T] or null.
 */
inline fun <reified T : Fragment> FragmentManager.findFragment(tag: String) = findFragmentByTag(tag) as? T?

/**
 * Create a [FragmentTransaction] body.
 * @param body the [FragmentTransaction] body.
 * @return [FragmentTransaction] body.
 */
fun FragmentTransaction(body: FragmentTransaction.() -> Unit) = body

/**
 * Attach [Fragment] to a host.
 * @receiver the [Fragment] that needs to be bound to.
 * @param host the host that needs to be bound to, must be [FragmentActivity] or [Fragment].
 * @param container the container view that needs to be bound to, must be a [View] or view id ([Int]),
 * default is get the root view from [host].
 * @param tag the [Fragment] tag, leave null to generate random tag.
 * @param customAnimId the [Fragment] transition animation, see [FragmentTransaction.setCustomAnimations].
 * @param allowStateLoss whether to allow state loss, default is true.
 * @param generateViewId whether to generate a view id for the resolved container when it has no id, default is true.
 * @param body the commit [FragmentTransaction] body.
 * @throws IllegalStateException if [host] or [container] unresolved.
 */
@JvmOverloads
fun Fragment.attach(
    host: LifecycleOwner,
    container: Any? = null,
    tag: String? = null,
    @AnimRes customAnimId: Int? = null,
    allowStateLoss: Boolean = true,
    generateViewId: Boolean = true,
    body: FragmentTransaction.() -> Unit = {}
) {
    host.fragmentManager().commit(allowStateLoss) {
        val containerViewId = container.resolveFragmentContainer(host.requireRootView(generateViewId), generateViewId)
        customAnimId?.also { setCustomAnimations(it, 0) }
        add(containerViewId, this@attach, tag ?: generateRandomTag())

        body()
    }
}

/**
 * Use the current [Fragment] from host to replace the existing one.
 * @receiver the [Fragment] that needs to be replaced to.
 * @param host the host that needs to be replaced to, must be [FragmentActivity] or [Fragment].
 * @param container the container view that needs to be replaced to, must be a [View] or view id ([Int]),
 * default is get the root view from [host].
 * @param tag the [Fragment] tag, leave null to generate random tag.
 * @param customEnterAnimId the [Fragment] transition enter animation, see [FragmentTransaction.setCustomAnimations].
 * @param customExitAnimId the [Fragment] transition exit animation, see [FragmentTransaction.setCustomAnimations].
 * @param allowStateLoss whether to allow state loss, default is true.
 * @param generateViewId whether to generate a view id for the resolved container when it has no id, default is true.
 * @param body the commit [FragmentTransaction] body.
 * @throws IllegalStateException if [host] or [container] unresolved.
 */
@JvmOverloads
fun Fragment.replace(
    host: LifecycleOwner,
    container: Any? = null,
    tag: String? = null,
    @AnimRes customEnterAnimId: Int? = null,
    @AnimRes customExitAnimId: Int? = null,
    allowStateLoss: Boolean = true,
    generateViewId: Boolean = true,
    body: FragmentTransaction.() -> Unit = {}
) {
    host.fragmentManager().commit(allowStateLoss) {
        val containerViewId = container.resolveFragmentContainer(host.requireRootView(generateViewId), generateViewId)

        if (customEnterAnimId != null || customExitAnimId != null)
            setCustomAnimations(customEnterAnimId ?: 0, customExitAnimId ?: 0)
        replace(containerViewId, this@replace, tag ?: generateRandomTag())

        body()
    }
}

/**
 * Show the current [Fragment].
 *
 * If [Fragment.isAdded] returns false, this function will no effect.
 * @receiver the current [Fragment].
 * @param host the current host of this [Fragment], must be [FragmentActivity] or [Fragment],
 * default is current [Fragment.parentFragment] or [Fragment.requireActivity].
 * @param customAnimId the [Fragment] transition animation.
 * @param allowStateLoss whether to allow state loss, default is true.
 * @param body the commit [FragmentTransaction] body.
 */
@JvmOverloads
fun Fragment.show(
    host: LifecycleOwner = parentFragment ?: requireActivity(),
    @AnimRes customAnimId: Int? = null,
    allowStateLoss: Boolean = true,
    body: FragmentTransaction.() -> Unit = {}
) {
    if (!checkIsAdded(action = "show")) return

    host.fragmentManager().commit(allowStateLoss) {
        customAnimId?.also { setCustomAnimations(it, 0) }
        show(this@show)

        body()
    }
}

/**
 * Hide the current [Fragment].
 *
 * If [Fragment.isAdded] returns false, this function will no effect.
 * @receiver the current [Fragment].
 * @param host the current host of this [Fragment], must be [FragmentActivity] or [Fragment],
 * default is current [Fragment.parentFragment] or [Fragment.requireActivity].
 * @param customAnimId the [Fragment] transition animation.
 * @param allowStateLoss whether to allow state loss, default is true.
 * @param body the commit [FragmentTransaction] body.
 */
@JvmOverloads
fun Fragment.hide(
    host: LifecycleOwner = parentFragment ?: requireActivity(),
    @AnimRes customAnimId: Int? = null,
    allowStateLoss: Boolean = true,
    body: FragmentTransaction.() -> Unit = {}
) {
    if (!checkIsAdded(action = "hide")) return

    host.fragmentManager().commit(allowStateLoss) {
        customAnimId?.also { setCustomAnimations(0, it) }
        hide(this@hide)

        body()
    }
}

/**
 * Detach and remove [Fragment] from host.
 *
 * If [Fragment.isAdded] returns false, this function will no effect.
 * @receiver the [Fragment] that needs to be removed.
 * @param host the current host of this [Fragment], must be [FragmentActivity] or [Fragment],
 * default is current [Fragment.parentFragment] or [Fragment.requireActivity].
 * @param customAnimId the [Fragment] transition animation.
 * @param allowStateLoss whether to allow state loss, default is true.
 * @param body the commit [FragmentTransaction] body.
 */
@JvmOverloads
fun Fragment.detach(
    host: LifecycleOwner = parentFragment ?: requireActivity(),
    @AnimRes customAnimId: Int? = null,
    allowStateLoss: Boolean = true,
    body: FragmentTransaction.() -> Unit = {}
) {
    if (!checkIsAdded(action = "detach")) return

    host.fragmentManager().commit(allowStateLoss) {
        customAnimId?.also { setCustomAnimations(0, it) }
        remove(this@detach)

        body()
    }
}

/**
 * Get the [FragmentManager] from [FragmentActivity] or [Fragment].
 * @receiver the current [FragmentActivity] or [Fragment].
 * @return [FragmentManager]
 * @throws IllegalStateException if the host type is not [FragmentActivity] or [Fragment].
 */
private fun LifecycleOwner.fragmentManager() = when (this) {
    is FragmentActivity -> fragmentManager()
    is Fragment -> fragmentManager()
    else -> error("The host type must be FragmentActivity or Fragment, but got ${this.javaClass}.")
}

/**
 * Get the root view from current host.
 * @receiver the current [LifecycleOwner].
 * @param generateViewId whether to generate a view id when the resolved root view has no id.
 * @return [ViewGroup]
 */
private fun LifecycleOwner.requireRootView(generateViewId: Boolean) = when (this) {
    is FragmentActivity -> findViewById<ViewGroup>(Android_R.id.content)?.let {
        it.firstChildOrNull<ViewGroup>()?.apply { ensureFragmentContainerId(generateViewId) }
            ?: it.apply { ensureFragmentContainerId(generateViewId) }
    } ?: error("FragmentActivity require a root view that is a ViewGroup, also tried android.R.id.content.")
    is Fragment -> (requireView() as? ViewGroup?)?.apply { ensureFragmentContainerId(generateViewId) }
        ?: error("Fragment require a root view that is a ViewGroup.")
    else -> error("The host type must be FragmentActivity or Fragment, but got ${this.javaClass}.")
}

/**
 * Resolve the [Fragment] container view id.
 * @receiver the container that needs to be bound to.
 * @param default the default container view.
 * @param generateViewId whether to generate a view id when the container view has no id.
 * @return [Int] container view id.
 * @throws IllegalStateException if the container view is not resolved.
 */
private fun Any?.resolveFragmentContainer(default: View, generateViewId: Boolean): Int {
    val containerViewId = when (this) {
        is Int -> this
        is View -> this.apply {
            // If the view id is not set, generate a new id.
            ensureFragmentContainerId(generateViewId)
        }.id
        null -> default.id
        else -> error("The container view type must be Int or View, but got ${this.javaClass}.")
    }.takeIf { it != View.NO_ID }

    return containerViewId ?: error("Fragment needs to be attached to an existing view.")
}

/**
 * Generate or validate the id of the Fragment container view.
 */
private fun View.ensureFragmentContainerId(generateViewId: Boolean) {
    if (id != View.NO_ID) return
    if (generateViewId) id = View.generateViewId()
    else error("Fragment container view $this has no id, please set an id or enable generateViewId.")
}

/**
 * Check if the [Fragment] is added to the host.
 * @receiver the current [Fragment].
 * @param action the action name.
 * @return [Boolean]
 */
private fun Fragment.checkIsAdded(action: String): Boolean {
    val mIsAdded = isAdded
    if (!mIsAdded) Log.w(BetterAndroidProperties.PROJECT_NAME, "[$action] Fragment $this is not added to the host.")

    return mIsAdded
}

/**
 * Generate a random tag for [Fragment].
 * @receiver the current fragment.
 * @return [String] random tag.
 */
private fun Fragment.generateRandomTag() = "${this}_${System.currentTimeMillis()}_${(999..9999).random()}"