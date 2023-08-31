import androidx.compose.ui.window.ComposeUIViewController
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.cache.storage.CacheStorage
import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import ui.MainScaffold

actual fun getPlatformName(): String = "iOS"
actual val appVersion: String = "1.0"

actual val language: String?
    get() = NSLocale.currentLocale.languageCode

actual val country: String?
    get() = NSLocale.currentLocale.countryCode

actual fun getCacheFile(): CacheStorage {
    return CacheStorage.Unlimited()
}

actual fun getPlatformHttpClient(): HttpClient {
    return HttpClient(Darwin) {
        engine {
            configureRequest {
                setAllowsCellularAccess(true)
            }
        }
    }
}

actual val screenHeightPixels: Int = 0
actual val screenWidthPixels: Int = 0
actual val screenDensityDPI: Int = 0

fun MainViewController() = ComposeUIViewController { MainScaffold() }