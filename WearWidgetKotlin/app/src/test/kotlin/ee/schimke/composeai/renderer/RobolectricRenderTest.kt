package ee.schimke.composeai.renderer

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
