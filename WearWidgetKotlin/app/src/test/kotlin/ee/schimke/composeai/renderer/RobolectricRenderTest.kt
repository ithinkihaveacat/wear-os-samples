/*
 * Copyright 2026 The Android Open Source Project
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
 */
package ee.schimke.composeai.renderer

import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner

@RunWith(ParameterizedRobolectricTestRunner::class)
class RobolectricRenderTest(
    preview: RenderPreviewEntry,
    @Suppress("UNCHECKED_CAST") previewArgs: List<Any?>,
) : RobolectricRenderTestBase(preview, previewArgs) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun previews(): List<Array<Any>> {
            val all = PreviewManifestLoader.loadShard(0, 1)
            println("RobolectricRenderTest: loaded ${all.size} previews")
            val filtered =
                all.filter { array ->
                    val preview = array[0] as RenderPreviewEntry
                    val keep =
                        !preview.id.contains("CollapsibleColumnSample1Preview") &&
                            !preview.id.contains("TouchGestureSample1Preview") &&
                            !preview.id.contains("VerticalScrollSamplePreview")
                    keep
                }
            println("RobolectricRenderTest: kept ${filtered.size} previews")
            return filtered
        }
    }
}
