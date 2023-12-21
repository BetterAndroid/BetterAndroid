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
import android.view.ViewGroup
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.highcapable.betterandroid.ui.extension.R
import com.highcapable.betterandroid.ui.extension.generated.BetterAndroidProperties
import android.R as Android_R

/**
 * Start a [Fragment] transaction and commit.
 * @see FragmentManager.beginTransaction
 * @receiver [FragmentManager]
 * @param allowingStateLoss whether to allow state loss, default true.
 * @param initiate the [FragmentTransaction] builder body.
 * @return [Int]
 */
inline fun FragmentManager.commitTransaction(allowingStateLoss: Boolean = true, initiate: FragmentTransaction.() -> Unit) =
    beginTransaction()
        .apply(initiate)
        .let { if (allowingStateLoss) it.commitAllowingStateLoss() else it.commit() }

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
 * - This function is deprecated, use [FragmentManager.commitTransaction] instead.
 * @see FragmentManager.commitTransaction
 */
@Deprecated(
    message = "Use FragmentManager.commitTransaction instead.",
    replaceWith = ReplaceWith("supportFragmentManager.commitTransaction(isAllowingStateLoss, initiate)")
)
inline fun FragmentActivity.commitFragmentTransaction(isAllowingStateLoss: Boolean = true, initiate: FragmentTransaction.() -> Unit) {
    supportFragmentManager.commitTransaction(isAllowingStateLoss, initiate)
}

/**
 * - This function is deprecated, use [FragmentManager.commitTransaction] instead.
 * @see FragmentManager.commitTransaction
 */
@Deprecated(
    message = "Use FragmentManager.commitTransaction instead.",
    replaceWith = ReplaceWith("childFragmentManager.commitTransaction(isAllowingStateLoss, initiate)")
)
inline fun Fragment.commitFragmentTransaction(isAllowingStateLoss: Boolean = true, initiate: FragmentTransaction.() -> Unit) {
    childFragmentManager.commitTransaction(isAllowingStateLoss, initiate)
}

/**
 * - This function is deprecated, use [FragmentManager.findFragment] instead.
 * @see FragmentManager.findFragment
 */
@Deprecated(
    message = "Use FragmentManager.findFragment instead.",
    replaceWith = ReplaceWith("supportFragmentManager.findFragment(id)")
)
inline fun <reified T : Fragment> FragmentActivity.findFragment(@IdRes id: Int) = supportFragmentManager.findFragment<T>(id)

/**
 * - This function is deprecated, use [FragmentManager.findFragment] instead.
 * @see FragmentManager.findFragment
 */
@Deprecated(
    message = "Use FragmentManager.findFragment instead.",
    replaceWith = ReplaceWith("supportFragmentManager.findFragment(tag)")
)
inline fun <reified T : Fragment> FragmentActivity.findFragment(tag: String) = supportFragmentManager.findFragment<T>(tag)

/**
 * - This function is deprecated, use [FragmentManager.findFragment] instead.
 * @see FragmentManager.findFragment
 */
@Deprecated(
    message = "Use FragmentManager.findFragment instead.",
    replaceWith = ReplaceWith("childFragmentManager.findFragment(id)")
)
inline fun <reified T : Fragment> Fragment.findFragment(@IdRes id: Int) = childFragmentManager.findFragment<T>(id)

/**
 * - This function is deprecated, use [FragmentManager.findFragment] instead.
 * @see FragmentManager.findFragment
 */
@Deprecated(
    message = "Use FragmentManager.findFragment instead.",
    replaceWith = ReplaceWith("childFragmentManager.findFragment(tag)")
)
inline fun <reified T : Fragment> Fragment.findFragment(tag: String) = childFragmentManager.findFragment<T>(tag)

/**
 * Attach [Fragment] to [FragmentActivity].
 * @param activity the [FragmentActivity] that needs to be bound to.
 * @param viewId the container view id that needs to be bound to.
 * @param view the container that needs to be bound to, default is [Activity.requireRootView].
 * @param beginAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param finishAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param tag the [Fragment] tag, default is [System.currentTimeMillis].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param allowingStateLoss whether to allow state loss, default true.
 * @throws IllegalStateException if [view] is null.
 */
@JvmOverloads
fun Fragment.attachToActivity(
    activity: FragmentActivity,
    @IdRes viewId: Int = View.NO_ID,
    view: View? = activity.requireRootView(),
    @AnimRes beginAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_open_out,
    tag: String = System.currentTimeMillis().toString(),
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
    activity.supportFragmentManager.commitTransaction(allowingStateLoss) {
        setCustomAnimations(beginAnimId, finishAnimId)
        add(viewId.takeIf { it != View.NO_ID } ?: view?.id ?: error("Fragment need attached to a view."), this@attachToActivity, tag)
        runOnCommit?.also { runOnCommit(it) }
    }
}

/**
 * Attach child [Fragment] to [Fragment].
 * @param fragment the [Fragment] that needs to be bound to.
 * @param viewId the container view id that needs to be bound to.
 * @param view the container that needs to be bound to, default is [Fragment.requireRootView].
 * @param beginAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param finishAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param tag the [Fragment] tag, default is [System.currentTimeMillis].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param allowingStateLoss whether to allow state loss, default true.
 * @throws IllegalStateException if [view] is null.
 */
@JvmOverloads
fun Fragment.attachToFragment(
    fragment: Fragment,
    @IdRes viewId: Int = View.NO_ID,
    view: View? = fragment.requireRootView(),
    @AnimRes beginAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_open_out,
    tag: String = System.currentTimeMillis().toString(),
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
    fragment.childFragmentManager.commitTransaction(allowingStateLoss) {
        setCustomAnimations(beginAnimId, finishAnimId)
        add(viewId.takeIf { it != View.NO_ID } ?: view?.id ?: error("Fragment need attached to a view."), this@attachToFragment, tag)
        runOnCommit?.also { runOnCommit(it) }
    }
}

/**
 * Use the current [Fragment] from [FragmentActivity] to replace the existing one.
 * @param activity the [FragmentActivity] that needs to be replace to.
 * @param viewId the container view id that needs to be replace to.
 * @param view the container that needs to be replace to, default is [Activity.requireRootView].
 * @param beginAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param finishAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param tag the [Fragment] tag, default is [System.currentTimeMillis].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param allowingStateLoss whether to allow state loss, default true.
 * @throws IllegalStateException if [view] is null.
 */
@JvmOverloads
fun Fragment.replaceFromActivity(
    activity: FragmentActivity,
    @IdRes viewId: Int = View.NO_ID,
    view: View? = activity.requireRootView(),
    @AnimRes beginAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_open_out,
    tag: String = System.currentTimeMillis().toString(),
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
    activity.supportFragmentManager.commitTransaction(allowingStateLoss) {
        setCustomAnimations(beginAnimId, finishAnimId)
        replace(viewId.takeIf { it != View.NO_ID } ?: view?.id ?: error("Fragment need attached to a view."), this@replaceFromActivity, tag)
        runOnCommit?.also { runOnCommit(it) }
    }
}

/**
 * Use the current child [Fragment] from [Fragment] to replace the existing one.
 * @param fragment the [Fragment] that needs to be replace to.
 * @param viewId the container view id that needs to be replace to.
 * @param view the container that needs to be replace to, default is [Fragment.requireRootView].
 * @param beginAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param finishAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param tag the [Fragment] tag, default is [System.currentTimeMillis].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param allowingStateLoss whether to allow state loss, default true.
 * @throws IllegalStateException if [view] is null.
 */
@JvmOverloads
fun Fragment.replaceFromFragment(
    fragment: Fragment,
    @IdRes viewId: Int = View.NO_ID,
    view: View? = fragment.requireRootView(),
    @AnimRes beginAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_open_out,
    tag: String = System.currentTimeMillis().toString(),
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
    fragment.childFragmentManager.commitTransaction(allowingStateLoss) {
        setCustomAnimations(beginAnimId, finishAnimId)
        replace(viewId.takeIf { it != View.NO_ID } ?: view?.id ?: error("Fragment need attached to a view."), this@replaceFromFragment, tag)
        runOnCommit?.also { runOnCommit(it) }
    }
}

/**
 * Show the current [Fragment].
 * @param activity the current [FragmentActivity], default is [Fragment.getActivity].
 * @param fragment the current parent [Fragment], default is [Fragment.getParentFragment].
 * @param beginAnimId the [Fragment] start to enter animation, default is [R.anim.simple_ui_open_in].
 * @param finishAnimId the [Fragment] finished enter animation, default is [R.anim.simple_ui_open_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param allowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun Fragment.show(
    activity: FragmentActivity? = getActivity(),
    fragment: Fragment? = parentFragment,
    @AnimRes beginAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_open_out,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
    /** Begin. */
    fun FragmentTransaction.begin() {
        setCustomAnimations(beginAnimId, finishAnimId)
        show(this@show)
        runOnCommit?.also { runOnCommit(it) }
    }
    fragment?.childFragmentManager?.commitTransaction(allowingStateLoss) { begin() }
        ?: activity?.supportFragmentManager?.commitTransaction(allowingStateLoss) { begin() }
        ?: Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to show Fragment $this, FragmentActivity or parent Fragment is null.")
}

/**
 * Hide the current [Fragment].
 * @param activity the current [FragmentActivity], default is [Fragment.getActivity].
 * @param fragment the current parent [Fragment], default is [Fragment.getParentFragment].
 * @param beginAnimId the [Fragment] start to exit animation, default is [R.anim.simple_ui_exit_in].
 * @param finishAnimId the [Fragment] finished exit animation, default is [R.anim.simple_ui_exit_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param allowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun Fragment.hide(
    activity: FragmentActivity? = getActivity(),
    fragment: Fragment? = parentFragment,
    @AnimRes beginAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
    /** Begin. */
    fun FragmentTransaction.begin() {
        setCustomAnimations(beginAnimId, finishAnimId)
        hide(this@hide)
        runOnCommit?.also { runOnCommit(it) }
    }
    fragment?.childFragmentManager?.commitTransaction(allowingStateLoss) { begin() }
        ?: activity?.supportFragmentManager?.commitTransaction(allowingStateLoss) { begin() }
        ?: Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to hide Fragment $this, FragmentActivity or parent Fragment is null.")
}

/**
 * Detach and remove [Fragment] from [FragmentActivity].
 * @param activity the current [FragmentActivity], default is [Fragment.getActivity].
 * @param beginAnimId the [Fragment] start to exit animation, default is [R.anim.simple_ui_exit_in].
 * @param finishAnimId the [Fragment] finished exit animation, default is [R.anim.simple_ui_exit_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param allowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun Fragment.detachFromActivity(
    activity: FragmentActivity? = getActivity(),
    @AnimRes beginAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
    activity?.supportFragmentManager?.commitTransaction(allowingStateLoss) {
        setCustomAnimations(beginAnimId, finishAnimId)
        remove(this@detachFromActivity)
        runOnCommit?.also { runOnCommit(it) }
    } ?: Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to remove Fragment $this, FragmentActivity is null.")
}

/**
 * Detach and remove child [Fragment] from [Fragment].
 * @param fragment the current parent [Fragment], default is [Fragment.getParentFragment].
 * @param beginAnimId the [Fragment] start to exit animation, default is [R.anim.simple_ui_exit_in].
 * @param finishAnimId the [Fragment] finished exit animation, default is [R.anim.simple_ui_exit_out].
 * @param runOnCommit the commit [Runnable], default is null.
 * @param allowingStateLoss whether to allow state loss, default true.
 */
@JvmOverloads
fun Fragment.detachFromFragment(
    fragment: Fragment? = parentFragment,
    @AnimRes beginAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
    fragment?.childFragmentManager?.commitTransaction(allowingStateLoss) {
        setCustomAnimations(beginAnimId, finishAnimId)
        remove(this@detachFromFragment)
        runOnCommit?.also { runOnCommit(it) }
    } ?: Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to remove Fragment $this, parent Fragment is null.")
}

/**
 * Batch attach [Fragment] to [FragmentActivity].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun FragmentActivity.attachFragments(
    vararg fragments: Fragment,
    @IdRes viewId: Int = View.NO_ID,
    view: View? = requireRootView(),
    @AnimRes beginAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_open_out,
    tag: String = System.currentTimeMillis().toString(),
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}

/**
 * Batch attach child [Fragment] to [Fragment].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun Fragment.attachFragments(
    vararg fragments: Fragment,
    @IdRes viewId: Int = View.NO_ID,
    view: View? = requireRootView(),
    @AnimRes beginAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_open_out,
    tag: String = System.currentTimeMillis().toString(),
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}

/**
 * Batch show [Fragment] from [FragmentActivity].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun FragmentActivity.showFragments(
    vararg fragments: Fragment,
    @AnimRes beginAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_open_out,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}

/**
 * Batch show child [Fragment] from [Fragment].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun Fragment.showFragments(
    vararg fragments: Fragment,
    @AnimRes beginAnimId: Int = R.anim.simple_ui_open_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_open_out,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}

/**
 * Batch hide [Fragment] from [FragmentActivity].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun FragmentActivity.hideFragments(
    vararg fragments: Fragment,
    @AnimRes beginAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}

/**
 * Batch hide child [Fragment] from [Fragment].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun Fragment.hideFragments(
    vararg fragments: Fragment,
    @AnimRes beginAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}

/**
 * Batch detach and remove [Fragment] from [FragmentActivity].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun FragmentActivity.detachFragments(
    vararg fragments: Fragment,
    @AnimRes beginAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}

/**
 * Batch detach and remove child [Fragment] from [Fragment].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun Fragment.detachFragments(
    vararg fragments: Fragment,
    @AnimRes beginAnimId: Int = R.anim.simple_ui_exit_in,
    @AnimRes finishAnimId: Int = R.anim.simple_ui_exit_out,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}

/**
 * Get the root view from current activity.
 * @receiver the current activity.
 * @return [ViewGroup]
 */
private fun Activity.requireRootView() = findViewById<ViewGroup>(Android_R.id.content).let {
    (it.getChildAt(0) as? ViewGroup?)?.apply { if (id == View.NO_ID) id = View.generateViewId() } ?: it
}

/**
 * Get the root view from current fragment.
 *
 * If the view is no id, it will be add a random id.
 * @receiver the current fragment.
 * @return [ViewGroup]
 * @throws IllegalStateException if the root view is not a [ViewGroup].
 */
private fun Fragment.requireRootView() = (requireView() as? ViewGroup?)?.apply { if (id == View.NO_ID) id = View.generateViewId() }
    ?: error("Fragment require a root view that is a ViewGroup.")