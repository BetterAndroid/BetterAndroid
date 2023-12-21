/*
 * Better Android - Create more useful tool extensions for Android.
 * Copyright (C) 2019-2023 HighCapable
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
@file:Suppress("unused")
@file:JvmName("FragmentUtils")

package com.highcapable.betterandroid.ui.extension.component

import android.app.Activity
import android.util.Log
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.highcapable.betterandroid.ui.extension.R
import com.highcapable.betterandroid.ui.extension.generated.BetterAndroidProperties
import android.R as Android_R

/**
 * Through [FragmentActivity.getSupportFragmentManager] to start [Fragment] transaction and commit.
 * @receiver the current activity.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 * @param initiate the [FragmentTransaction] builder body.
 */
inline fun FragmentActivity.commitFragmentTransaction(isAllowingStateLoss: Boolean = true, initiate: FragmentTransaction.() -> Unit) {
    supportFragmentManager
        .beginTransaction()
        .apply(initiate)
        .apply { if (isAllowingStateLoss) commitAllowingStateLoss() else commit() }
}

/**
 * Through [Fragment.getChildFragmentManager] to start [Fragment] transaction and commit.
 * @receiver the current fragment.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 * @param initiate the [FragmentTransaction] builder body.
 */
inline fun Fragment.commitFragmentTransaction(isAllowingStateLoss: Boolean = true, initiate: FragmentTransaction.() -> Unit) {
    childFragmentManager
        .beginTransaction()
        .apply(initiate)
        .apply { if (isAllowingStateLoss) commitAllowingStateLoss() else commit() }
}

/**
 * Through [FragmentActivity.getSupportFragmentManager] to find exists [Fragment].
 * @receiver the current activity.
 * @param id the [Fragment] ID to find.
 * @return [T] or null.
 */
inline fun <reified T : Fragment> FragmentActivity.findFragment(@IdRes id: Int) = supportFragmentManager.findFragmentById(id) as? T?

/**
 * Through [FragmentActivity.getSupportFragmentManager] to find exists [Fragment].
 * @receiver the current activity.
 * @param tag the [Fragment] tag to find.
 * @return [T] or null.
 */
inline fun <reified T : Fragment> FragmentActivity.findFragment(tag: String) = supportFragmentManager.findFragmentByTag(tag) as? T?

/**
 * Through [Fragment.getChildFragmentManager] to find exists [Fragment].
 * @receiver the current fragment.
 * @param id the [Fragment] ID to find.
 * @return [T] or null.
 */
inline fun <reified T : Fragment> Fragment.findFragment(@IdRes id: Int) = childFragmentManager.findFragmentById(id) as? T?

/**
 * Through [Fragment.getChildFragmentManager] to find exists [Fragment].
 * @receiver the current fragment.
 * @param tag the [Fragment] tag to find.
 * @return [T] or null.
 */
inline fun <reified T : Fragment> Fragment.findFragment(tag: String) = childFragmentManager.findFragmentByTag(tag) as? T?

/**
 * Attach [Fragment] to [FragmentActivity].
 * @param activity the [FragmentActivity] that needs to be bound to.
 * @param view the container that needs to be bound to, default is [Activity.rootView].
 * @param viewId the container view id that needs to be bound to.
 * @param enterAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param exitAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param tag the [Fragment] tag, default is [System.currentTimeMillis].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 * @throws IllegalStateException if [view] is null.
 */
@JvmOverloads
fun Fragment.attachToActivity(
    activity: FragmentActivity,
    view: View? = activity.rootView(),
    @IdRes viewId: Int = -1,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_open_out,
    tag: String = System.currentTimeMillis().toString(),
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    activity.commitFragmentTransaction(isAllowingStateLoss) {
        setCustomAnimations(enterAnimId, exitAnimId)
        add(viewId.takeIf { it >= 0 } ?: view?.id ?: error("Fragment need attached to a view."), this@attachToActivity, tag)
        runOnCommit?.also { runOnCommit(it) }
    }
}

/**
 * Attach child [Fragment] to [Fragment].
 * @param fragment the [Fragment] that needs to be bound to.
 * @param view the container that needs to be bound to, default is [Fragment.rootView].
 * @param viewId the container view id that needs to be bound to.
 * @param enterAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param exitAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param tag the [Fragment] tag, default is [System.currentTimeMillis].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 * @throws IllegalStateException if [view] is null.
 */
@JvmOverloads
fun Fragment.attachToFragment(
    fragment: Fragment,
    view: View? = fragment.rootView(),
    @IdRes viewId: Int = -1,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_open_out,
    tag: String = System.currentTimeMillis().toString(),
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    fragment.commitFragmentTransaction(isAllowingStateLoss) {
        setCustomAnimations(enterAnimId, exitAnimId)
        add(viewId.takeIf { it >= 0 } ?: view?.id ?: error("Fragment need attached to a view."), this@attachToFragment, tag)
        runOnCommit?.also { runOnCommit(it) }
    }
}

/**
 * Batch attach [Fragment] to [FragmentActivity].
 * @param fragments the array of [Fragment] that needs to be bound to.
 * @param view the container that needs to be bound to, default is [Activity.rootView].
 * @param viewId the container view id that needs to be bound to.
 * @param enterAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param exitAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param tag the [Fragment] tag, default is [System.currentTimeMillis].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun FragmentActivity.attachFragments(
    vararg fragments: Fragment,
    view: View? = rootView(),
    @IdRes viewId: Int = -1,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_open_out,
    tag: String = System.currentTimeMillis().toString(),
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    fragments.takeIf { it.isNotEmpty() }
        ?.forEach { it.attachToActivity(this, view, viewId, enterAnimId, exitAnimId, tag, { runOnCommit?.run() }, isAllowingStateLoss) }
}

/**
 * Batch attach child [Fragment] to [Fragment].
 * @param fragments the array of [Fragment] that needs to be bound to.
 * @param view the container that needs to be bound to, default is [Fragment.rootView].
 * @param viewId the container view id that needs to be bound to.
 * @param enterAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param exitAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param tag the [Fragment] tag, default is [System.currentTimeMillis].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun Fragment.attachFragments(
    vararg fragments: Fragment,
    view: View? = rootView(),
    @IdRes viewId: Int = -1,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_open_out,
    tag: String = System.currentTimeMillis().toString(),
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    fragments.takeIf { it.isNotEmpty() }
        ?.forEach { it.attachToFragment(this, view, viewId, enterAnimId, exitAnimId, tag, { runOnCommit?.run() }, isAllowingStateLoss) }
}

/**
 * Use the current [Fragment] from [FragmentActivity] to replace the existing one.
 * @param activity the [FragmentActivity] that needs to be replace to.
 * @param view the container that needs to be replace to, default is [Activity.rootView].
 * @param viewId the container view id that needs to be replace to.
 * @param enterAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param exitAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param tag the [Fragment] tag, default is [System.currentTimeMillis].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 * @throws IllegalStateException if [view] is null.
 */
@JvmOverloads
fun Fragment.replaceFromActivity(
    activity: FragmentActivity,
    view: View? = activity.rootView(),
    @IdRes viewId: Int = -1,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_open_out,
    tag: String = System.currentTimeMillis().toString(),
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    activity.commitFragmentTransaction(isAllowingStateLoss) {
        setCustomAnimations(enterAnimId, exitAnimId)
        replace(viewId.takeIf { it >= 0 } ?: view?.id ?: error("Fragment need attached to a view."), this@replaceFromActivity, tag)
        runOnCommit?.also { runOnCommit(it) }
    }
}

/**
 * Use the current child [Fragment] from [Fragment] to replace the existing one.
 * @param fragment the [Fragment] that needs to be replace to.
 * @param view the container that needs to be replace to, default is [Fragment.rootView].
 * @param viewId the container view id that needs to be replace to.
 * @param enterAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param exitAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param tag the [Fragment] tag, default is [System.currentTimeMillis].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 * @throws IllegalStateException if [view] is null.
 */
@JvmOverloads
fun Fragment.replaceFromFragment(
    fragment: Fragment,
    view: View? = fragment.rootView(),
    @IdRes viewId: Int = -1,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_open_out,
    tag: String = System.currentTimeMillis().toString(),
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    fragment.commitFragmentTransaction(isAllowingStateLoss) {
        setCustomAnimations(enterAnimId, exitAnimId)
        replace(viewId.takeIf { it >= 0 } ?: view?.id ?: error("Fragment need attached to a view."), this@replaceFromFragment, tag)
        runOnCommit?.also { runOnCommit(it) }
    }
}

/**
 * Show the current [Fragment].
 * @param activity the current [FragmentActivity], default is [Fragment.getActivity].
 * @param fragment the current parent [Fragment], if you using [attachToFragment], [replaceFromFragment],
 * please use this parameter.
 * @param enterAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param exitAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun Fragment.show(
    activity: FragmentActivity? = getActivity(),
    fragment: Fragment? = null,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_open_out,
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    /** Begin. */
    fun FragmentTransaction.begin() {
        setCustomAnimations(enterAnimId, exitAnimId)
        show(this@show)
        runOnCommit?.also { runOnCommit(it) }
    }
    fragment?.commitFragmentTransaction { begin() }
        ?: activity?.commitFragmentTransaction(isAllowingStateLoss) { begin() }
        ?: Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to show Fragment $this, FragmentActivity or parent Fragment is null.")
}

/**
 * Hide the current [Fragment].
 * @param activity the current [FragmentActivity], default is [Fragment.getActivity].
 * @param fragment the current parent [Fragment], if you using [attachToFragment], [replaceFromFragment],
 * please use this parameter.
 * @param enterAnimId the [Fragment] start to exit animation, default is [R.anim.simple_ui_exit_in].
 * @param exitAnimId the [Fragment] finished exit animation, default is [R.anim.simple_ui_exit_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun Fragment.hide(
    activity: FragmentActivity? = getActivity(),
    fragment: Fragment? = null,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    /** Begin. */
    fun FragmentTransaction.begin() {
        setCustomAnimations(enterAnimId, exitAnimId)
        hide(this@hide)
        runOnCommit?.also { runOnCommit(it) }
    }
    fragment?.commitFragmentTransaction { begin() }
        ?: activity?.commitFragmentTransaction(isAllowingStateLoss) { begin() }
        ?: Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to hide Fragment $this, FragmentActivity or parent Fragment is null.")
}

/**
 * Batch show [Fragment] from [FragmentActivity].
 * @param fragments the array of [Fragment] that needs to be show.
 * @param enterAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param exitAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun FragmentActivity.showFragments(
    vararg fragments: Fragment,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_open_out,
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    fragments.takeIf { it.isNotEmpty() }?.forEach {
        it.show(
            activity = this,
            enterAnimId = enterAnimId,
            exitAnimId = exitAnimId,
            runOnCommit = { runOnCommit?.run() },
            isAllowingStateLoss = isAllowingStateLoss
        )
    }
}

/**
 * Batch show child [Fragment] from [Fragment].
 * @param fragments the array of [Fragment] that needs to be show.
 * @param enterAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param exitAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun Fragment.showFragments(
    vararg fragments: Fragment,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_open_out,
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    fragments.takeIf { it.isNotEmpty() }?.forEach {
        it.show(
            fragment = this,
            enterAnimId = enterAnimId,
            exitAnimId = exitAnimId,
            runOnCommit = { runOnCommit?.run() },
            isAllowingStateLoss = isAllowingStateLoss
        )
    }
}

/**
 * Batch hide [Fragment] from [FragmentActivity].
 * @param fragments the array of [Fragment] that needs to be hide.
 * @param enterAnimId the [Fragment] start to exit animation, default is [R.anim.simple_ui_exit_in].
 * @param exitAnimId the [Fragment] finished exit animation, default is [R.anim.simple_ui_exit_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun FragmentActivity.hideFragments(
    vararg fragments: Fragment,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    fragments.takeIf { it.isNotEmpty() }?.forEach {
        it.hide(
            activity = this,
            enterAnimId = enterAnimId,
            exitAnimId = exitAnimId,
            runOnCommit = { runOnCommit?.run() },
            isAllowingStateLoss = isAllowingStateLoss
        )
    }
}

/**
 * Batch hide child [Fragment] from [Fragment].
 * @param fragments the array of [Fragment] that needs to be hide.
 * @param enterAnimId the [Fragment] start to exit animation, default is [R.anim.simple_ui_exit_in].
 * @param exitAnimId the [Fragment] finished exit animation, default is [R.anim.simple_ui_exit_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun Fragment.hideFragments(
    vararg fragments: Fragment,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    fragments.takeIf { it.isNotEmpty() }?.forEach {
        it.hide(
            fragment = this,
            enterAnimId = enterAnimId,
            exitAnimId = exitAnimId,
            runOnCommit = { runOnCommit?.run() },
            isAllowingStateLoss = isAllowingStateLoss
        )
    }
}

/**
 * Detach and remove [Fragment] from [FragmentActivity].
 * @param activity the current [FragmentActivity], default is [Fragment.getActivity].
 * @param enterAnimId the [Fragment] start to exit animation, default is [R.anim.simple_ui_exit_in].
 * @param exitAnimId the [Fragment] finished exit animation, default is [R.anim.simple_ui_exit_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun Fragment.detachFromActivity(
    activity: FragmentActivity? = getActivity(),
    @AnimRes enterAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    activity?.commitFragmentTransaction(isAllowingStateLoss) {
        setCustomAnimations(enterAnimId, exitAnimId)
        remove(this@detachFromActivity)
        runOnCommit?.also { runOnCommit(it) }
    } ?: Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to remove Fragment $this, FragmentActivity is null.")
}

/**
 * Detach and remove child [Fragment] from [Fragment].
 * @param fragment the current parent [Fragment].
 * @param enterAnimId the [Fragment] start to exit animation, default is [R.anim.simple_ui_exit_in].
 * @param exitAnimId the [Fragment] finished exit animation, default is [R.anim.simple_ui_exit_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun Fragment.detachFromFragment(
    fragment: Fragment?,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    fragment?.commitFragmentTransaction(isAllowingStateLoss) {
        setCustomAnimations(enterAnimId, exitAnimId)
        remove(this@detachFromFragment)
        runOnCommit?.also { runOnCommit(it) }
    } ?: Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to remove Fragment $this, parent Fragment is null.")
}

/**
 * Batch detach and remove [Fragment] from [FragmentActivity].
 * @param fragments the array of [Fragment] that needs to be detach.
 * @param enterAnimId the [Fragment] start to exit animation, default is [R.anim.simple_ui_exit_in].
 * @param exitAnimId the [Fragment] finished exit animation, default is [R.anim.simple_ui_exit_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun FragmentActivity.detachFragments(
    vararg fragments: Fragment,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    fragments.takeIf { it.isNotEmpty() }
        ?.forEach { it.detachFromActivity(this, enterAnimId, exitAnimId, { runOnCommit?.run() }, isAllowingStateLoss) }
}

/**
 * Batch detach and remove child [Fragment] from [Fragment].
 * @param fragments the array of [Fragment] that needs to be detach.
 * @param enterAnimId the [Fragment] start to exit animation, default is [R.anim.simple_ui_exit_in].
 * @param exitAnimId the [Fragment] finished exit animation, default is [R.anim.simple_ui_exit_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param isAllowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun Fragment.detachFragments(
    vararg fragments: Fragment,
    @AnimRes enterAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes exitAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    isAllowingStateLoss: Boolean = true
) {
    fragments.takeIf { it.isNotEmpty() }
        ?.forEach { it.detachFromFragment(this, enterAnimId, exitAnimId, { runOnCommit?.run() }, isAllowingStateLoss) }
}

/**
 * Get the [Android_R.id.content] from current activity.
 * @receiver the current activity.
 * @return [View]
 */
private fun Activity.rootView() = findViewById<View>(Android_R.id.content)

/**
 * Get the [Fragment.requireView] from current fragment.
 *
 * If the view is no id, it will be add a random id.
 * @receiver the current fragment.
 * @return [View]
 */
private fun Fragment.rootView() = requireView().apply { if (id == View.NO_ID) id = (999..99999).random() }