package com.google.example.wear_widget

import ee.schimke.composeai.renderer.RobolectricRenderTestBase
import ee.schimke.composeai.renderer.RenderPreviewEntry
import ee.schimke.composeai.renderer.PreviewManifestLoader
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner

@RunWith(ParameterizedRobolectricTestRunner::class)
class RobolectricRenderTest(preview: RenderPreviewEntry) : RobolectricRenderTestBase(preview) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun previews(): List<Array<Any>> = PreviewManifestLoader.loadShard(0, 1)
    }
}
