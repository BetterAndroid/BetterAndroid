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
@file:Suppress("unused", "MemberVisibilityCanBePrivate", "SpellCheckingInspection")

package com.highcapable.betterandroid.system.extension.utils

import androidx.annotation.IntDef
import com.highcapable.betterandroid.system.extension.utils.RomType.current
import com.highcapable.betterandroid.system.extension.utils.RomType.matches
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.kavaref.extension.hasClass
import com.highcapable.kavaref.extension.lazyClassOrNull

/**
 * Android ROMs type tool.
 *
 * Here is a collection of third-party in-depth customization of the identification functions of
 * Android ROMs from some major manufacturers.
 *
 * The identification function relies on reading the [Class] implementation that exists specifically
 * in the system framework of the current running environment through Java reflection and [SystemProperties].
 *
 * Some of the later ROM detection additions were inspired by
 * [DeviceCompat](https://github.com/getActivity/DeviceCompat).
 * Special thanks to that project for the adaptation ideas and references.
 */
object RomType {

    /**
     * The ROM type value annotation.
     */
    @IntDef(
        DEFAULT,
        HYPEROS,
        MIUI,
        COLOROS,
        REALMEUI,
        FUNTOUCHOS,
        ORIGINOS,
        MAGICOS,
        FLYME,
        ONEUI,
        OXYGENOS,
        H2OS,
        ZUI,
        ZUXOS,
        REDMAGICOS,
        NEBULAAIOS,
        MYOS,
        MIFAVORUI,
        NUBIAUI,
        SMARTISANOS,
        EUI,
        OBRICUI,
        ROGUI,
        OS360,
        HARMONYOS,
        HARMONYOS_NEXT,
        EMUI,
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
    const val DEFAULT = 0x00

    /** [HyperOS](https://hyperos.mi.com/) */
    const val HYPEROS = 0x01

    /** [MIUI](https://home.miui.com/) */
    const val MIUI = 0x02

    /** [ColorOS](https://www.coloros.com/) */
    const val COLOROS = 0x03

    /** [realmeUI](https://www.realme.com/) */
    const val REALMEUI = 0x04

    /** [FuntouchOS](https://www.vivo.com/funtouchos) */
    const val FUNTOUCHOS = 0x05

    /** [OriginOS](https://www.vivo.com/originos) */
    const val ORIGINOS = 0x06

    /** [MagicOS](https://www.honor.com/) */
    const val MAGICOS = 0x07

    /** [Flyme](https://flyme.com/) */
    const val FLYME = 0x08

    /** [OneUI](https://www.samsung.com/one-ui) */
    const val ONEUI = 0x09

    /** [OxygenOS](https://www.oneplus.com/) */
    const val OXYGENOS = 0x10

    /** [H2OS](https://www.oneplus.com/) */
    const val H2OS = 0x11

    /** [ZUI](https://zui.com/) / [ZUXOS](https://zuxos.com/) */
    const val ZUI = 0x12

    /** [ZUXOS](https://zuxos.com/) */
    const val ZUXOS = 0x13

    /** [RedMagicOS](https://www.nubia.com/) */
    const val REDMAGICOS = 0x14

    /** [NebulaAIOS](https://www.nubia.com/) */
    const val NEBULAAIOS = 0x15

    /** [MyOS](https://www.nubia.com/) */
    const val MYOS = 0x16

    /** [MifavorUI](https://www.nubia.com/) */
    const val MIFAVORUI = 0x17

    /** [NubiaUI](https://www.nubia.com/) */
    const val NUBIAUI = 0x18

    /** [SmartisanOS](https://www.smartisan.com/os/) */
    const val SMARTISANOS = 0x19

    /** [EUI](https://www.tuyidesign.com/cases/leshi/) */
    const val EUI = 0x20

    /** [ObricUI](https://www.doubao.com/) */
    const val OBRICUI = 0x21

    /** [RogUI](https://www.asus.com/) */
    const val ROGUI = 0x22

    /** [360OS](https://www.360os.com/) */
    const val OS360 = 0x23

    /** [HarmonyOS](https://www.harmonyos.com/) */
    const val HARMONYOS = 0x24

    /**
     * [HarmonyOS NEXT](https://www.harmonyos.com/)
     *
     * Refers to the Android compatible environment on HarmonyOS NEXT.
     */
    const val HARMONYOS_NEXT = 0x25

    /** [EMUI](https://www.huaweicentral.com/emui) */
    const val EMUI = 0x26

    /** [VisionOS](https://fans.hisense.com/forum-269-1.html) */
    const val VISIONOS = 0x27

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
    val current by lazy {
        when {
            Matcher.HyperOS -> HYPEROS
            Matcher.Miui -> MIUI
            Matcher.ColorOS -> COLOROS
            Matcher.RealmeUi -> REALMEUI
            Matcher.OriginOS -> ORIGINOS
            Matcher.FuntouchOS -> FUNTOUCHOS
            Matcher.MagicOS -> MAGICOS
            Matcher.Flyme -> FLYME
            Matcher.OneUi -> ONEUI
            Matcher.OxygenOS -> OXYGENOS
            Matcher.H2OS -> H2OS
            Matcher.NebulaAiOS -> NEBULAAIOS
            Matcher.RedmagicOS -> REDMAGICOS
            Matcher.MyOS -> MYOS
            Matcher.MifavorUi -> MIFAVORUI
            Matcher.ZuxOS -> ZUXOS
            Matcher.ZUi -> ZUI
            Matcher.NubiaUi -> NUBIAUI
            Matcher.ObricUi -> OBRICUI
            Matcher.RogUi -> ROGUI
            Matcher.SmartisanOS -> SMARTISANOS
            Matcher.EUi -> EUI
            Matcher.OS360 -> OS360
            Matcher.HarmonyOS -> HARMONYOS
            Matcher.HarmonyOSNext -> HARMONYOS_NEXT
            Matcher.Emui -> EMUI
            Matcher.VisionOS -> VISIONOS
            else -> DEFAULT
        }
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

        private val propContainsMiOS by lazy {
            hasAnyProperties("ro.mi.os.version.name", "ro.mi.os.version.code", "ro.mi.os.version.incremental")
        }

        private val propIsMiui816 by lazy { SystemProperties.get("ro.miui.ui.version.name") == "V816" }
        private val classHasMiuiR by lazy { "android.miui.R".exists() }

        private val classHasVivoSysFactory by lazy { "com.vivo.VivoSystemFrameworkFactory".exists() }
        private val classHasVivoR by lazy { "vivo.R".exists() }
        private val propVivoOsDisplayId by lazy { SystemProperties.get("ro.vivo.os.build.display.id") }

        private val propOneUiVersion by lazy { SystemProperties.get("ro.build.version.oneui") }
        private val propZteOsVersion by lazy { SystemProperties.get("ro.build.MiFavor_version") }

        private val propDisplayId by lazy { SystemProperties.get("ro.build.display.id") }
        private val propNubiaRomName by lazy { SystemProperties.get("ro.build.nubia.rom.name") }
        private val propNubiaRomCode by lazy { SystemProperties.get("ro.build.nubia.rom.code") }
        private val propZuxOsName by lazy { SystemProperties.get("ro.config.lgsi.os.name") }

        private val HuaweiBuildExClass by lazyClassOrNull("com.huawei.system.BuildEx")

        private val isLegacyRedmagicByNubiaUi by lazy {
            AndroidVersion.isAtLeast(AndroidVersion.O_MR1) &&
                propNubiaRomName.containsIgnoreCase("nubiaui") &&
                (AndroidVersion.name.extract() - propNubiaRomCode.extract()) >= 5
        }

        private val buildExSaysHarmonyOS by lazy {
            HuaweiBuildExClass?.resolve()?.optional(silent = true)?.firstMethodOrNull {
                name = "getOsBrand"
            }?.invokeQuietly<String?>().orEmpty().containsIgnoreCase("harmony")
        }

        val HyperOS by lazy { propContainsMiOS || (classHasMiuiR && propIsMiui816) }

        val Miui by lazy {
            !HyperOS && (hasAnyProperties("ro.miui.ui.version.name", "ro.miui.ui.version.code") || classHasMiuiR) &&
                !propContainsMiOS && !propIsMiui816
        }

        val ColorOS by lazy {
            !RealmeUi && (hasAnyProperties("ro.build.version.oplusrom", "ro.build.version.opporom") ||
                "oppo.R".exists() || "oplus.R".exists() || "com.color.os.ColorBuild".exists())
        }

        val RealmeUi by lazy { SystemProperties.contains("ro.build.version.realmeui") }

        val FuntouchOS by lazy {
            !OriginOS && (propVivoOsDisplayId.containsIgnoreCase("funtouch") || (classHasVivoR && !classHasVivoSysFactory))
        }

        val OriginOS by lazy { propVivoOsDisplayId.containsIgnoreCase("origin") || (classHasVivoR && classHasVivoSysFactory) }

        val MagicOS by lazy { hasAnyProperties("msc.config.magic.version", "ro.build.version.magic") }

        val Flyme by lazy {
            hasAnyProperties("ro.flyme.published", "ro.flyme.version.id") ||
                "flyme.app.IActivityManagerExt".exists() || "flyme.config.FlymeFeature".exists() ||
                "com.meizu.server.AppOpsHandle".exists()
        }

        val OneUi by lazy {
            propOneUiVersion.isNotBlank() ||
                "com.samsung.android.ProductPackagesRune".exists() || "com.samsung.epic.request".exists() ||
                "knox.security.keystore.KnoxAndroidKeyStoreSpi".exists()
        }

        val OxygenOS by lazy { SystemProperties.contains("ro.oxygen.version") }

        val H2OS by lazy { !OxygenOS && !ColorOS && SystemProperties.contains("ro.rom.version") }

        val ZuxOS by lazy { propZuxOsName.containsIgnoreCase("zuxos") }

        val ZUi by lazy {
            !ZuxOS && (hasAnyProperties(
                "ro.com.zui.version",
                "ro.zui.version.status",
                "ro.zui.hardware.displayid",
                "persist.radio.zui.feature",
                "ro.config.zuisdk.enabled"
            ) || "com.zui.internal.app.IAppFaceService".exists() || "zuisdk.app.AlertActivity".exists() ||
                "zui.icon.ExtraResources".exists() || "com.zui.internal.app.ZuiShutdownActivity".exists())
        }

        val RedmagicOS by lazy {
            propDisplayId.containsIgnoreCase("redmagicos") || propZteOsVersion.containsIgnoreCase("redmagicos") ||
                isLegacyRedmagicByNubiaUi ||
                "cn.nubia.internal.R".exists() || "com.nubia.internal.R".exists() ||
                "cn.nubia.tcsystem.INubiaTcSystemCallback".exists() || "com.nubia.tcsystem.INubiaTcSystemCallback".exists()
        }

        val NebulaAiOS by lazy {
            propDisplayId.containsIgnoreCase("nebulaaios") || propZteOsVersion.containsIgnoreCase("nebulaaios")
        }

        val MyOS by lazy {
            propDisplayId.containsIgnoreCase("myos") || propZteOsVersion.containsIgnoreCase("myos")
        }

        val MifavorUi by lazy {
            !NebulaAiOS && !RedmagicOS && !MyOS && propZteOsVersion.containsIgnoreCase("zte")
        }

        val NubiaUi by lazy {
            !RedmagicOS && (propNubiaRomName.containsIgnoreCase("nubiaui") ||
                (("com.zte.PlatformConfig".exists() || "com.zte.zsdk.IPolicyManager".exists() ||
                    "zpub.res.R".exists()) && "nubia.util.BlurUtil".exists())
            )
        }

        val SmartisanOS by lazy { hasAnyProperties("ro.smartisan.sa", "ro.smartisan.version") }

        val EUi by lazy {
            hasAnyProperties(
                "ro.letv.release.version",
                "ro.letv.release.version_date",
                "ro.product.letv_model",
                "ro.product.letv_name",
                "sys.letv.fmodelaid",
                "persist.sys.leui.bootreason",
                "ro.config.leui_ringtone_slot2",
                "ro.leui_oem_unlock_enable"
            )
        }

        val ObricUi by lazy { SystemProperties.contains("init.svc.bytecellular") }

        val OS360 by lazy { SystemProperties.containsIgnoreCase("ro.build.uiversion", "360ui") }

        val RogUi by lazy {
            SystemProperties.contains("ro.asus.rog") ||
                "com.asus.cta.CtaAction".exists() || "com.asus.ims.rogproxy.IRogProxy".exists()
        }

        val HarmonyOS by lazy {
            !HarmonyOSNext && (hasAnyProperties("ro.build.ohos.devicetype", "persist.sys.ohos.osd.cloud.switch") ||
                buildExSaysHarmonyOS ||
                ("ohos.system.version.SystemVersion".exists() && SystemProperties.contains("ro.build.ohos.devicetype") &&
                    hasAnyProperties("ro.build.hide.matchers", "ro.build.hide.replacements"))
            )
        }

        val HarmonyOSNext by lazy {
            hasAnyProperties(
                "ro.product.anco.devicetype",
                "ro.sys.anco.product.software.version",
                "ro.product.os.dist.anco.apiversion",
                "ro.product.os.dist.anco.releasetype"
            )
        }

        val Emui by lazy {
            !MagicOS && !HarmonyOS && (SystemProperties.containsIgnoreCase("ro.build.version.emui", "emotionui") ||
                "androidhwext.R".exists() || "com.huawei.android.app.HwActivityManager".exists())
        }

        val VisionOS by lazy {
            "com.hmct.epd.EpdManager".exists() || "com.hmct.facelock.IDetectedCallback".exists() ||
                "com.hmct.ThemeUtils.ConfigNotifier".exists() || "com.hmct.ThemeUtils.FontUtil".exists() ||
                "com.hmct.ThemeUtils.FontUtilException".exists() || "com.hmct.ThemeUtils.ThemeUtil".exists()
        }

        private fun String.exists() = javaClass.classLoader?.hasClass(this) == true

        private fun hasAnyProperties(vararg keys: String) = keys.any { SystemProperties.contains(it) }
        private fun SystemProperties.containsIgnoreCase(key: String, value: String) =
            this.get(key).contains(value, ignoreCase = true)

        private fun String.containsIgnoreCase(value: String) = this.contains(value, ignoreCase = true)
        private fun String.extract() = """\d+""".toRegex().find(this)?.value?.toIntOrNull() ?: 0
    }
}