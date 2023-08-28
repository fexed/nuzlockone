import androidx.compose.runtime.Composable
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.FileStorage
import ui.MainScaffold
import java.io.File
import java.util.Locale

actual fun getPlatformName(): String = "Android"
actual val appVersion: String = "1.1"

actual val language: String?
    get() = Locale.getDefault().language

actual val country: String?
    get() = Locale.getDefault().country

lateinit var cacheFilesDir: File
actual fun getCacheFile(): CacheStorage {
    return FileStorage(cacheFilesDir)
}

actual fun getPlatformHttpClient(): HttpClient {
    return HttpClient()
}

@Composable
fun MainView() = MainScaffold()