import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.storage.CacheStorage
import network.Cache

expect fun getPlatformName(): String
expect val language: String?
expect val country: String?
expect val appVersion: String
expect val screenHeightPixels: Int
expect val screenWidthPixels: Int
expect val screenDensityDPI: Int
const val sharedVersion: String = "1.0"
val cache = Cache.instance

@Composable
fun Int.pxToDp() = with (LocalDensity.current) { this@pxToDp.toDp() }

enum class WindowSizeClass { COMPACT, MEDIUM, EXPANDED }

@Composable
fun widthClass(): WindowSizeClass {
    return when {
        screenWidthPixels.pxToDp() < 600.dp -> WindowSizeClass.COMPACT
        screenWidthPixels.pxToDp() < 840.dp -> WindowSizeClass.MEDIUM
        else -> WindowSizeClass.EXPANDED
    }
}

@Composable
fun heightClass(): WindowSizeClass {
    return when {
        screenHeightPixels.pxToDp() < 480.dp -> WindowSizeClass.COMPACT
        screenHeightPixels.pxToDp() < 900.dp -> WindowSizeClass.MEDIUM
        else -> WindowSizeClass.EXPANDED
    }

    // Use widthWindowSizeClass and heightWindowSizeClass
}

expect fun getCacheFile(): CacheStorage
expect fun getPlatformHttpClient(): HttpClient