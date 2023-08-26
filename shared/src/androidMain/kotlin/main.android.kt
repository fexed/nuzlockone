import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.storage.CacheStorage
import ui.MainScaffold
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

@Preview
@Composable
fun AppPreview() {
    MainScaffold()
}