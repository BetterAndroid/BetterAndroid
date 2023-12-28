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
 * This file is created by fankes on 2023/10/22.
 */
@file:Suppress("unused", "CovariantEquals", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.system.extension.tool

import com.highcapable.yukireflection.factory.hasClass

/**
 * Android system kind tool.
 *
 * Here is a collection of third-party in-depth customization of the identification functions of
 * Android ROMs from some major manufacturers.
 *
 * The identification function relies on reading the [Class] implementation that exists specifically
 * in the system framework of the current running environment through Java reflection and [SystemProperties].
 */
object SystemKind {

    /**
     * The default kind.
     *
     * Also included some other third-party customization systems or native,
     * native-like and Pixel.
     */
    const val DEFAULT = 1000

    /** [HarmonyOS](https://www.harmonyos.com/) */
    const val HARMONYOS = 1001

    /** [EMUI](https://www.huaweicentral.com/emui) */
    const val EMUI = 1002

    /** [MIUI](https://home.miui.com/) */
    const val MIUI = 1003

    /** [HyperOS](https://hyperos.mi.com/) */
    const val HYPEROS = 1004

    /**
     * [ColorOS](https://www.coloros.com/)
     *
     * Also included RealmeUI and OxygenOS.
     */
    const val COLOROS = 1005

    /** [FuntouchOS](https://www.vivo.com/funtouchos) */
    const val FUNTOUCHOS = 1006

    /** [OriginOS](https://www.vivo.com/originos) */
    const val ORIGINOS = 1007

    /** [Flyme](https://flyme.com/) */
    const val FLYME = 1008

    /** [OneUI](https://www.samsung.com/one-ui) */
    const val ONEUI = 1009

    /** [ZUI](https://zui.com/) */
    const val ZUI = 1010

    /** [RedMagicOS](https://www.nubia.com/) */
    const val REDMAGICOS = 1011

    /** [NubiaUI](https://www.nubia.com/) */
    const val NUBIAUI = 1012

    /** [RogUI](https://www.asus.com/) */
    const val ROGUI = 1013

    /** [VisionOS](https://fans.hisense.com/forum-269-1.html) */
    const val VISIONOS = 1014

    /**
     * Get the current system kind.
     *
     * Returns the system kind declared in [SystemKind].
     *
     * Usage:
     *
     * ```kotlin
     * val kind = SystemKind.current
     * when (kind) {
     *     SystemKind.MIUI -> {
     *         // Do something.
     *     }
     * }
     * ```
     * @return [Int]
     */
    @JvmStatic
    val current by lazy {
        val harmonyOSKind = "ohos.system.version.SystemVersion".hasClass() && SystemProperties.contains("ro.build.ohos.devicetype") &&
            (SystemProperties.contains("ro.build.hide.matchers") || SystemProperties.contains("ro.build.hide.replacements"))
        val emuiKind = !harmonyOSKind && ("androidhwext.R".hasClass() || "com.huawei.android.app.HwActivityManager".hasClass())
        val propContainsMiOS = SystemProperties.contains("ro.mi.os.version.name")
        val propIsMiui816 = SystemProperties.get("ro.miui.ui.version.name") == "V816"
        val classHasMiuiR = "android.miui.R".hasClass()
        val miuiKind = classHasMiuiR && !propContainsMiOS && !propIsMiui816
        val hyperOSKind = classHasMiuiR && (propContainsMiOS || propIsMiui816)
        val colorOSKind = "oppo.R".hasClass() || "oplus.R".hasClass() || "com.color.os.ColorBuild".hasClass()
        val classHasVivoSysFactory = "com.vivo.VivoSystemFrameworkFactory".hasClass()
        val classHasVivoR = "vivo.R".hasClass()
        val funtouchOSKind = classHasVivoR && !classHasVivoSysFactory
        val originOSKind = classHasVivoR && classHasVivoSysFactory
        val flymeKind = "flyme.app.IActivityManagerExt".hasClass() || "flyme.config.FlymeFeature".hasClass() ||
            "com.meizu.server.AppOpsHandle".hasClass()
        val oneuiKind = "com.samsung.android.ProductPackagesRune".hasClass() || "com.samsung.epic.request".hasClass() ||
            "knox.security.keystore.KnoxAndroidKeyStoreSpi".hasClass()
        val zuiKind = "com.zui.internal.app.IAppFaceService".hasClass() || "zuisdk.app.AlertActivity".hasClass() ||
            "zui.icon.ExtraResources".hasClass()
        val redmagicOSKind = "cn.nubia.internal.R".hasClass() || "com.nubia.internal.R".hasClass() ||
            "cn.nubia.tcsystem.INubiaTcSystemCallback".hasClass() || "com.nubia.tcsystem.INubiaTcSystemCallback".hasClass()
        val nubiauiKind = ("com.zte.PlatformConfig".hasClass() || "com.zte.zsdk.IPolicyManager".hasClass() ||
            "zpub.res.R".hasClass()) && "nubia.util.BlurUtil".hasClass()
        val roguiKind = "com.asus.cta.CtaAction".hasClass() || "com.asus.ims.rogproxy.IRogProxy".hasClass()
        val visionOSKind = "com.hmct.epd.EpdManager".hasClass() || "com.hmct.facelock.IDetectedCallback".hasClass() ||
            "com.hmct.ThemeUtils.ConfigNotifier".hasClass() || "com.hmct.ThemeUtils.FontUtil".hasClass() ||
            "com.hmct.ThemeUtils.FontUtilException".hasClass() || "com.hmct.ThemeUtils.ThemeUtil".hasClass()
        when {
            harmonyOSKind -> HARMONYOS
            emuiKind -> EMUI
            miuiKind -> MIUI
            hyperOSKind -> HYPEROS
            colorOSKind -> COLOROS
            funtouchOSKind -> FUNTOUCHOS
            originOSKind -> ORIGINOS
            flymeKind -> FLYME
            oneuiKind -> ONEUI
            zuiKind -> ZUI
            redmagicOSKind -> REDMAGICOS
            nubiauiKind -> NUBIAUI
            roguiKind -> ROGUI
            visionOSKind -> VISIONOS
            else -> DEFAULT
        }
    }

    /**
     * Get the current system kind.
     *
     * - This function is deprecated, use [current] instead.
     * @see current
     */
    @Deprecated(message = "Use current instead.", ReplaceWith("current"))
    @JvmStatic
    fun get() = current

    /**
     * Determine whether the current system is of the specific kind.
     *
     * If [kind] is filled with a system kind that exceeds the preset range,
     * it will always return false.
     *
     * Usage:
     *
     * ```kotlin
     * val isMiui = SystemKind.equals(SystemKind.MIUI)
     * ```
     * @param kind the system kind declared in [SystemKind].
     * @return [Boolean]
     */
    @JvmStatic
    fun equals(kind: Int) = current == kind
}