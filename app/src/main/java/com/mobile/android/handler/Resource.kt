/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobile.android.handler

/**
 *  Taken directly from
 *  https://github.com/android/architecture-components-samples/tree/88747993139224a4bb6dbe985adf652d557de621/GithubBrowserSample
 *  Since I didn't see this is much needed
 *
 * A generic class that holds a value with its loading status.
 * @param <T>
 *     (val status: Status, val data: T?, val message: String?)
</T> */
sealed class Resource<out T> {
    data class Success<T>(val data: T): Resource<T>()
    data class Error<T>(val msg: String, val data: T?): Resource<T>()
    data class Loading<T>(val data: T?): Resource<T>()
}