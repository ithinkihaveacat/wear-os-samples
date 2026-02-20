package presentation

import kotlin.math.roundToInt

data class WearDevice(
    val id: String,
    val modelName: String,
    val screenSizePx: Int,
    val density: Float,
    val fontScale: Float = 1f,
    val isRound: Boolean = true
) {
    val dp: Int = (screenSizePx / density).roundToInt()
    val dpi: Int = (density * 160).roundToInt()

    companion object {
        val MobvoiTicWatchPro5 = WearDevice(
            id = "ticwatch_pro_5",
            modelName = "Mobvoi TicWatch Pro 5",
            screenSizePx = 466,
            density = 2.0f
        )
        val SamsungGalaxyWatch5 = WearDevice(
            id = "galaxy_watch_5",
            modelName = "Samsung Galaxy Watch 5",
            screenSizePx = 396,
            density = 2.0f
        )
        val SamsungGalaxyWatch6 = WearDevice(
            id = "galaxy_watch_6",
            modelName = "Samsung Galaxy Watch 6 Large",
            screenSizePx = 480,
            density = 2.125f
        )
        val GooglePixelWatch = WearDevice(
            id = "pixel_watch",
            modelName = "Google Pixel Watch",
            screenSizePx = 384,
            density = 2.0f
        )
        val GenericSmallRound = WearDevice(
            id = "small_round",
            modelName = "Generic Small Round",
            screenSizePx = 384,
            density = 2.0f
        )
        val GenericLargeRound = WearDevice(
            id = "large_round",
            modelName = "Generic Large Round",
            screenSizePx = 454,
            density = 2.0f
        )
        val SamsungGalaxyWatch6SmallFont = SamsungGalaxyWatch6.copy(
            id = "galaxy_watch_6_small_font",
            fontScale = 0.94f
        )
        val GooglePixelWatchLargeFont = GooglePixelWatch.copy(
            id = "pixel_watch_large_font",
            fontScale = 1.24f
        )
        
        @JvmStatic
        val entries: List<WearDevice> = listOf(
            MobvoiTicWatchPro5,
            SamsungGalaxyWatch5,
            SamsungGalaxyWatch6,
            GooglePixelWatch,
            GenericSmallRound,
            GenericLargeRound,
            SamsungGalaxyWatch6SmallFont,
            GooglePixelWatchLargeFont
        )
    }
}
