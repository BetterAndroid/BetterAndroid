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
 * This file is created by fankes on 2025/12/14.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.ui.extension.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.highcapable.betterandroid.generated.BetterAndroidProperties
import com.highcapable.betterandroid.ui.extension.lint.detector.ToastUsageDetector

class UiExtensionIssueRegistry : IssueRegistry() {

    override val issues get() = listOf(
        ToastUsageDetector.ISSUE
    )

    override val minApi = BetterAndroidProperties.PROJECT_UI_EXTENSION_LINT_MIN_API
    override val api = CURRENT_API
    override val vendor = Vendor(
        vendorName = BetterAndroidProperties.PROJECT_NAME,
        identifier = BetterAndroidProperties.PROJECT_UI_EXTENSION_LINT_IDENTIFIER,
        feedbackUrl = "${BetterAndroidProperties.PROJECT_URL}/issues",
        contact = BetterAndroidProperties.PROJECT_URL
    )
}