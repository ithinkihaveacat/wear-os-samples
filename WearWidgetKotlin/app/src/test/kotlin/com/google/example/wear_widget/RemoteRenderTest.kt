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
package com.google.example.wear_widget

import ee.schimke.composeai.renderer.PreviewManifestLoader
import ee.schimke.composeai.renderer.RenderPreviewEntry
import ee.schimke.composeai.renderer.RobolectricRenderTestBase
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner

@RunWith(ParameterizedRobolectricTestRunner::class)
class RobolectricRenderTest(preview: RenderPreviewEntry) : RobolectricRenderTestBase(preview) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun previews(): List<Array<Any>> =
            PreviewManifestLoader.loadShard(0, 1).filter { array ->
                val preview = array[0] as RenderPreviewEntry
                val keep =
                    !preview.id.contains("CollapsibleColumnSample1Preview") &&
                        !preview.id.contains("TouchGestureSample1Preview") &&
                        !preview.id.contains("VerticalScrollSamplePreview")
                keep
            }
    }
}
