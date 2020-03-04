/*
 * Copyright (c) 2020 Halcyon Mobile.
 * https://www.halcyonmobile.com
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

package com.halcyonmobile.viewmodelfactory.processor

import java.io.File
import java.nio.file.Paths

/**
+ * Helper class which read the file in the resources folder with the given [fileName] into a string, each line separated with [lineDelimiter].
+ */
fun Any.readResourceFileToString(fileName: String): String {
    val path = this::class.java.classLoader.getResource(fileName).toURI().path
    return File(Paths.get(path).toUri()).readText()
}