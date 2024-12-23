/*
 * Better Android - Create more useful tool extensions for Android.
 * Copyright (C) 2019-2024 HighCapable
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
import androidx.fragment.app.commitNow
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
 * @param tag the [Fragment] tag, default is random tag.
 * @param customAnimId the [Fragment] transition animation, see [FragmentTransaction.setCustomAnimations].
 * @param allowStateLoss whether to allow state loss, default is true.
 * @param body the commit [FragmentTransaction] body.
 * @throws IllegalStateException if [host] or [container] unresolved.
 */
@JvmOverloads
fun Fragment.attach(
    host: LifecycleOwner,
    container: Any? = null,
    tag: String = generateRandomTag(),
    @AnimRes customAnimId: Int? = null,
    allowStateLoss: Boolean = true,
    body: FragmentTransaction.() -> Unit = {}
) {
    host.fragmentManager().commit(allowStateLoss) {
        val containerViewId = container.resolveFragmentContainer(host.requireRootView())
        customAnimId?.also { setCustomAnimations(it, 0) }
        add(containerViewId, this@attach, tag)
        body()
    }
}

/**
 * Use the current [Fragment] from host to replace the existing one.
 * @receiver the [Fragment] that needs to be replace to.
 * @param host the host that needs to be replace to, must be [FragmentActivity] or [Fragment].
 * @param container the container view that needs to be replace to, must be a [View] or view id ([Int]),
 * default is get the root view from [host].
 * @param tag the [Fragment] tag, default is random tag.
 * @param customEnterAnimId the [Fragment] transition enter animation, see [FragmentTransaction.setCustomAnimations].
 * @param customExitAnimId the [Fragment] transition exit animation, see [FragmentTransaction.setCustomAnimations].
 * @param allowStateLoss whether to allow state loss, default is true.
 * @param body the commit [FragmentTransaction] body.
 * @throws IllegalStateException if [host] or [container] unresolved.
 */
@JvmOverloads
fun Fragment.replace(
    host: LifecycleOwner,
    container: Any? = null,
    tag: String = generateRandomTag(),
    @AnimRes customEnterAnimId: Int? = null,
    @AnimRes customExitAnimId: Int? = null,
    allowStateLoss: Boolean = true,
    body: FragmentTransaction.() -> Unit = {}
) {
    host.fragmentManager().commit(allowStateLoss) {
        val containerViewId = container.resolveFragmentContainer(host.requireRootView())
        if (customEnterAnimId != null || customExitAnimId != null)
            setCustomAnimations(customEnterAnimId ?: 0, customExitAnimId ?: 0)
        replace(containerViewId, this@replace, tag)
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
 * @return [ViewGroup]
 */
private fun LifecycleOwner.requireRootView() = when (this) {
    is FragmentActivity -> findViewById<ViewGroup>(Android_R.id.content)?.let {
        it.firstChildOrNull<ViewGroup>()?.apply { if (id == View.NO_ID) id = View.generateViewId() } ?: it
    } ?: error("FragmentActivity require a root view that is a ViewGroup, also tried android.R.id.content.")
    is Fragment -> (requireView() as? ViewGroup?)?.apply { if (id == View.NO_ID) id = View.generateViewId() }
        ?: error("Fragment require a root view that is a ViewGroup.")
    else -> error("The host type must be FragmentActivity or Fragment, but got ${this.javaClass}.")
}

/**
 * Resolve the [Fragment] container view id.
 * @receiver the container that needs to be bound to.
 * @param default the default container view.
 * @return [Int] container view id.
 * @throws IllegalStateException if the container view is not resolved.
 */
private fun Any?.resolveFragmentContainer(default: View): Int {
    val containerViewId = when (this) {
        is Int -> this
        is View -> this.apply {
            // If the view id is not set, generate a new id.
            if (id == View.NO_ID) id = View.generateViewId()
        }.id
        null -> default.id
        else -> error("The container view type must be Int or View, but got ${this.javaClass}.")
    }.takeIf { it != View.NO_ID }
    return containerViewId ?: error("Fragment needs to be attached to an existing view.")
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
internal fun Fragment.generateRandomTag() = "${this}_${System.currentTimeMillis()}_${(999..9999).random()}"

// The following functions are deprecated, you should not use them.

/**
 * Attach [Fragment] to [FragmentActivity].
 *
 * - This function is deprecated and no effect, use [Fragment.attach] instead.
 * @see Fragment.attach
 */
@Suppress("UNUSED_PARAMETER", "UnusedReceiverParameter")
@Deprecated(message = "Use Fragment.attach instead.")
@JvmOverloads
fun Fragment.attachToActivity(
    activity: FragmentActivity,
    @IdRes viewId: Int = View.NO_ID,
    view: View? = null,
    @AnimRes animId: Int = 0,
    tag: String = System.currentTimeMillis().toString(),
    addToBackStack: String? = null,
    runOnCommit: Runnable? = null,
    allowStateLoss: Boolean = true
) {
}

/**
 * Attach child [Fragment] to [Fragment].
 *
 * - This function is deprecated and no effect, use [Fragment.attach] instead.
 * @see Fragment.attach
 */
@Suppress("UNUSED_PARAMETER", "UnusedReceiverParameter")
@Deprecated(message = "Use Fragment.attach instead.")
@JvmOverloads
fun Fragment.attachToFragment(
    fragment: Fragment,
    @IdRes viewId: Int = View.NO_ID,
    view: View? = null,
    @AnimRes animId: Int = 0,
    tag: String = System.currentTimeMillis().toString(),
    addToBackStack: String? = null,
    runOnCommit: Runnable? = null,
    allowStateLoss: Boolean = true
) {
}

/**
 * Use the current [Fragment] from [FragmentActivity] to replace the existing one.
 *
 * - This function is deprecated and no effect, use [Fragment.replace] instead.
 * @see Fragment.replace
 */
@Suppress("UNUSED_PARAMETER", "UnusedReceiverParameter")
@Deprecated(message = "Use Fragment.replace instead.")
@JvmOverloads
fun Fragment.replaceFromActivity(
    activity: FragmentActivity,
    @IdRes viewId: Int = View.NO_ID,
    view: View? = null,
    @AnimRes enterAnimId: Int = 0,
    @AnimRes exitAnimId: Int = 0,
    tag: String = System.currentTimeMillis().toString(),
    addToBackStack: String? = null,
    runOnCommit: Runnable? = null,
    allowStateLoss: Boolean = true
) {
}

/**
 * Use the current child [Fragment] from [Fragment] to replace the existing one.
 *
 * - This function is deprecated and no effect, use [Fragment.replace] instead.
 * @see Fragment.replace
 */
@Suppress("UNUSED_PARAMETER", "UnusedReceiverParameter")
@Deprecated(message = "Use Fragment.replace instead.")
@JvmOverloads
fun Fragment.replaceFromFragment(
    fragment: Fragment,
    @IdRes viewId: Int = View.NO_ID,
    view: View? = null,
    @AnimRes enterAnimId: Int = 0,
    @AnimRes exitAnimId: Int = 0,
    tag: String = System.currentTimeMillis().toString(),
    addToBackStack: String? = null,
    runOnCommit: Runnable? = null,
    allowStateLoss: Boolean = true
) {
}

/**
 * Show the current [Fragment].
 *
 * - This function is deprecated and no effect, use [Fragment.show] instead.
 * @see Fragment.show
 */
@Suppress("UNUSED_PARAMETER")
@Deprecated(message = "Use Fragment.show instead.")
@JvmName("show_Deprecated")
@JvmOverloads
fun Fragment.show(
    activity: FragmentActivity? = getActivity(),
    fragment: Fragment? = parentFragment,
    @AnimRes animId: Int = 0,
    runOnCommit: Runnable? = null,
    allowStateLoss: Boolean = true
) {
}

/**
 * Hide the current [Fragment].
 *
 * - This function is deprecated and no effect, use [Fragment.hide] instead.
 * @see Fragment.hide
 */
@Suppress("UNUSED_PARAMETER")
@Deprecated(message = "Use Fragment.hide instead.")
@JvmName("hide_Deprecated")
@JvmOverloads
fun Fragment.hide(
    activity: FragmentActivity? = getActivity(),
    fragment: Fragment? = parentFragment,
    @AnimRes animId: Int = 0,
    runOnCommit: Runnable? = null,
    allowStateLoss: Boolean = true
) {
}

/**
 * Detach and remove [Fragment] from [FragmentActivity].
 *
 * - This function is deprecated and no effect, use [Fragment.detach] instead.
 * @see Fragment.detach
 */
@Suppress("UNUSED_PARAMETER")
@Deprecated(message = "Use Fragment.detach instead.")
@JvmOverloads
fun Fragment.detachFromActivity(
    activity: FragmentActivity? = getActivity(),
    @AnimRes animId: Int = 0,
    runOnCommit: Runnable? = null,
    allowStateLoss: Boolean = true
) {
}

/**
 * Detach and remove child [Fragment] from [Fragment].
 *
 * - This function is deprecated and no effect, use [Fragment.detach] instead.
 * @see Fragment.detach
 */
@Suppress("UNUSED_PARAMETER")
@Deprecated(message = "Use Fragment.detach instead.")
@JvmOverloads
fun Fragment.detachFromFragment(
    fragment: Fragment? = parentFragment,
    @AnimRes animId: Int = 0,
    runOnCommit: Runnable? = null,
    allowStateLoss: Boolean = true
) {
}

/**
 * Start a [Fragment] transaction and commit.
 *
 * - This function is deprecated, use [FragmentManager.commit] or [FragmentManager.commitNow] instead.
 */
@Deprecated(
    message = "Use commit or commitNow instead.",
    replaceWith = ReplaceWith("commit(allowingStateLoss, initiate)")
)
inline fun FragmentManager.commitTransaction(allowingStateLoss: Boolean = true, initiate: FragmentTransaction.() -> Unit) =
    commit(allowingStateLoss, initiate)

/**
 * - This function is deprecated, use [FragmentManager.commitTransaction] instead.
 * @see FragmentManager.commitTransaction
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "Use FragmentManager.commitTransaction instead.",
    replaceWith = ReplaceWith("fragmentManager().commitTransaction(isAllowingStateLoss, initiate)")
)
inline fun FragmentActivity.commitFragmentTransaction(isAllowingStateLoss: Boolean = true, initiate: FragmentTransaction.() -> Unit) {
    fragmentManager().commitTransaction(isAllowingStateLoss, initiate)
}

/**
 * - This function is deprecated, use [FragmentManager.commitTransaction] instead.
 * @see FragmentManager.commitTransaction
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "Use FragmentManager.commitTransaction instead.",
    replaceWith = ReplaceWith("fragmentManager().commitTransaction(isAllowingStateLoss, initiate)")
)
inline fun Fragment.commitFragmentTransaction(isAllowingStateLoss: Boolean = true, initiate: FragmentTransaction.() -> Unit) {
    fragmentManager().commitTransaction(isAllowingStateLoss, initiate)
}

/**
 * - This function is deprecated, use [FragmentManager.findFragment] instead.
 * @see FragmentManager.findFragment
 */
@Deprecated(
    message = "Use FragmentManager.findFragment instead.",
    replaceWith = ReplaceWith("fragmentManager().findFragment(id)")
)
inline fun <reified T : Fragment> FragmentActivity.findFragment(@IdRes id: Int) = fragmentManager().findFragment<T>(id)

/**
 * - This function is deprecated, use [FragmentManager.findFragment] instead.
 * @see FragmentManager.findFragment
 */
@Deprecated(
    message = "Use FragmentManager.findFragment instead.",
    replaceWith = ReplaceWith("fragmentManager().findFragment(tag)")
)
inline fun <reified T : Fragment> FragmentActivity.findFragment(tag: String) = fragmentManager().findFragment<T>(tag)

/**
 * - This function is deprecated, use [FragmentManager.findFragment] instead.
 * @see FragmentManager.findFragment
 */
@Deprecated(
    message = "Use FragmentManager.findFragment instead.",
    replaceWith = ReplaceWith("fragmentManager().findFragment(id)")
)
inline fun <reified T : Fragment> Fragment.findFragment(@IdRes id: Int) = fragmentManager().findFragment<T>(id)

/**
 * - This function is deprecated, use [FragmentManager.findFragment] instead.
 * @see FragmentManager.findFragment
 */
@Deprecated(
    message = "Use FragmentManager.findFragment instead.",
    replaceWith = ReplaceWith("fragmentManager().findFragment(tag)")
)
inline fun <reified T : Fragment> Fragment.findFragment(tag: String) = fragmentManager().findFragment<T>(tag)

/**
 * Batch attach [Fragment] to [FragmentActivity].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UNUSED_PARAMETER", "UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun FragmentActivity.attachFragments(
    vararg fragments: Fragment,
    @IdRes viewId: Int = View.NO_ID,
    view: View? = null,
    @AnimRes beginAnimResId: Int = 0,
    @AnimRes finishAnimResId: Int = 0,
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
@Suppress("UNUSED_PARAMETER", "UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun Fragment.attachFragments(
    vararg fragments: Fragment,
    @IdRes viewId: Int = View.NO_ID,
    view: View? = null,
    @AnimRes beginAnimResId: Int = 0,
    @AnimRes finishAnimResId: Int = 0,
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
@Suppress("UNUSED_PARAMETER", "UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun FragmentActivity.showFragments(
    vararg fragments: Fragment,
    @AnimRes beginAnimResId: Int = 0,
    @AnimRes finishAnimResId: Int = 0,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}

/**
 * Batch show child [Fragment] from [Fragment].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UNUSED_PARAMETER", "UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun Fragment.showFragments(
    vararg fragments: Fragment,
    @AnimRes beginAnimResId: Int = 0,
    @AnimRes finishAnimResId: Int = 0,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}

/**
 * Batch hide [Fragment] from [FragmentActivity].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UNUSED_PARAMETER", "UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun FragmentActivity.hideFragments(
    vararg fragments: Fragment,
    @AnimRes beginAnimResId: Int = 0,
    @AnimRes finishAnimResId: Int = 0,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}

/**
 * Batch hide child [Fragment] from [Fragment].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UNUSED_PARAMETER", "UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun Fragment.hideFragments(
    vararg fragments: Fragment,
    @AnimRes beginAnimResId: Int = 0,
    @AnimRes finishAnimResId: Int = 0,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}

/**
 * Batch detach and remove [Fragment] from [FragmentActivity].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UNUSED_PARAMETER", "UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun FragmentActivity.detachFragments(
    vararg fragments: Fragment,
    @AnimRes beginAnimResId: Int = 0,
    @AnimRes finishAnimResId: Int = 0,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}

/**
 * Batch detach and remove child [Fragment] from [Fragment].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UNUSED_PARAMETER", "UnusedReceiverParameter")
@Deprecated(message = "No effect and will be removed in the future.")
@JvmOverloads
fun Fragment.detachFragments(
    vararg fragments: Fragment,
    @AnimRes beginAnimResId: Int = 0,
    @AnimRes finishAnimResId: Int = 0,
    runOnCommit: Runnable? = null,
    allowingStateLoss: Boolean = true
) {
}