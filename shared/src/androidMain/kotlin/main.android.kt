import androidx.compose.runtime.Composable
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.storage.CacheStorage
import java.util.Locale

actual fun getPlatformName(): String = "Android"

actual val language: String?
    get() = Locale.getDefault().language

actual val country: String?
    get() = Locale.getDefault().country

actual fun getCacheFile(): CacheStorage {
    return CacheStorage.Unlimited()
}

actual fun getPlatformHttpClient(): HttpClient {
    return HttpClient()
}

@Composable
fun MainView() = MainScaffold()
