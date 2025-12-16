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
 * This file is created by fankes on 2025/6/26.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.system.extension.utils

import androidx.annotation.IntDef
import com.highcapable.betterandroid.system.extension.utils.RomType.current
import com.highcapable.betterandroid.system.extension.utils.RomType.matches
import com.highcapable.kavaref.extension.hasClass

/**
 * Android ROMs type tool.
 *
 * Here is a collection of third-party in-depth customization of the identification functions of
 * Android ROMs from some major manufacturers.
 *
 * The identification function relies on reading the [Class] implementation that exists specifically
 * in the system framework of the current running environment through Java reflection and [SystemProperties].
 */
object RomType {

    /**
     * The ROM type value annotation.
     */
    @IntDef(
        DEFAULT,
        HARMONYOS,
        EMUI,
        MIUI,
        HYPEROS,
        COLOROS,
        FUNTOUCHOS,
        ORIGINOS,
        FLYME,
        ONEUI,
        ZUI,
        REDMAGICOS,
        NUBIAUI,
        ROGUI,
        VISIONOS
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Value

    /**
     * The default type.
     *
     * Also included some other third-party customization systems or stock Android,
     * AOSP-based Android system and Pixel.
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

    /** [ZUI](https://zui.com/) / [ZUXOS](https://zuxos.com/) */
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
     * Get the current ROM type.
     *
     * Returns the ROM type declared in [RomType].
     *
     * Usage:
     *
     * ```kotlin
     * val type = RomType.current
     * when (type) {
     *     RomType.MIUI -> {
     *         // Do something.
     *     }
     * }
     * ```
     * @see matches
     * @return [Int]
     */
    @JvmStatic
    val current get() = when {
        Matcher.HarmonyOS -> HARMONYOS
        Matcher.Emui -> EMUI
        Matcher.Miui -> MIUI
        Matcher.HyperOS -> HYPEROS
        Matcher.ColorOS -> COLOROS
        Matcher.FuntouchOS -> FUNTOUCHOS
        Matcher.OriginOS -> ORIGINOS
        Matcher.Flyme -> FLYME
        Matcher.OneUi -> ONEUI
        Matcher.ZUi -> ZUI
        Matcher.RedmagicOS -> REDMAGICOS
        Matcher.NubiaUi -> NUBIAUI
        Matcher.RogUi -> ROGUI
        Matcher.VisionOS -> VISIONOS
        else -> DEFAULT
    }

    /**
     * Determine whether the current ROM is of the specific type.
     *
     * If [type] is filled with a ROM type that exceeds the preset range,
     * it will always return false.
     *
     * Usage:
     *
     * ```kotlin
     * val isMiui = RomType.matches(RomType.MIUI)
     * ```
     * @see current
     * @param type the ROM type declared in [RomType].
     * @return [Boolean]
     */
    @JvmStatic
    fun matches(@Value type: Int) = current == type

    /**
     * A collection of type matchers for identifying the current ROM.
     */
    private object Matcher {

        private val propContainsMiOS by lazy { SystemProperties.contains("ro.mi.os.version.name") }
        private val propIsMiui816 by lazy { SystemProperties.get("ro.miui.ui.version.name") == "V816" }
        private val classHasMiuiR by lazy { "android.miui.R".exists() }

        private val classHasVivoSysFactory by lazy { "com.vivo.VivoSystemFrameworkFactory".exists() }
        private val classHasVivoR by lazy { "vivo.R".exists() }

        val HarmonyOS by lazy {
            "ohos.system.version.SystemVersion".exists() && SystemProperties.contains("ro.build.ohos.devicetype") &&
                (SystemProperties.contains("ro.build.hide.matchers") || SystemProperties.contains("ro.build.hide.replacements"))
        }

        val Emui by lazy {
            !HarmonyOS && ("androidhwext.R".exists() || "com.huawei.android.app.HwActivityManager".exists())
        }

        val Miui by lazy { classHasMiuiR && !propContainsMiOS && !propIsMiui816 }

        val HyperOS by lazy { classHasMiuiR && (propContainsMiOS || propIsMiui816) }

        val ColorOS by lazy {
            "oppo.R".exists() || "oplus.R".exists() || "com.color.os.ColorBuild".exists()
        }

        val FuntouchOS by lazy { classHasVivoR && !classHasVivoSysFactory }

        val OriginOS by lazy { classHasVivoR && classHasVivoSysFactory }

        val Flyme by lazy {
            "flyme.app.IActivityManagerExt".exists() || "flyme.config.FlymeFeature".exists() ||
                "com.meizu.server.AppOpsHandle".exists()
        }

        val OneUi by lazy {
            "com.samsung.android.ProductPackagesRune".exists() || "com.samsung.epic.request".exists() ||
                "knox.security.keystore.KnoxAndroidKeyStoreSpi".exists()
        }

        val ZUi by lazy {
            "com.zui.internal.app.IAppFaceService".exists() || "zuisdk.app.AlertActivity".exists() ||
                "zui.icon.ExtraResources".exists() || "com.zui.internal.app.ZuiShutdownActivity".exists()
        }

        val RedmagicOS by lazy {
            "cn.nubia.internal.R".exists() || "com.nubia.internal.R".exists() ||
                "cn.nubia.tcsystem.INubiaTcSystemCallback".exists() || "com.nubia.tcsystem.INubiaTcSystemCallback".exists()
        }

        val NubiaUi by lazy {
            ("com.zte.PlatformConfig".exists() || "com.zte.zsdk.IPolicyManager".exists() ||
                "zpub.res.R".exists()) && "nubia.util.BlurUtil".exists()
        }

        val RogUi by lazy {
            "com.asus.cta.CtaAction".exists() || "com.asus.ims.rogproxy.IRogProxy".exists()
        }

        val VisionOS by lazy {
            "com.hmct.epd.EpdManager".exists() || "com.hmct.facelock.IDetectedCallback".exists() ||
                "com.hmct.ThemeUtils.ConfigNotifier".exists() || "com.hmct.ThemeUtils.FontUtil".exists() ||
                "com.hmct.ThemeUtils.FontUtilException".exists() || "com.hmct.ThemeUtils.ThemeUtil".exists()
        }

        private fun String.exists() = javaClass.classLoader?.hasClass(this) == true
    }
}