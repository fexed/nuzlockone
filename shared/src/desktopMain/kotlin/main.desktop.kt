import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.FileStorage
import ui.MainScaffold
import java.awt.Toolkit
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Locale

actual fun getPlatformName(): String = "Desktop"
actual val appVersion: String = "1.1"

actual val language: String?
    get() = Locale.getDefault().language

actual val country: String?
    get() = Locale.getDefault().country

actual fun getCacheFile(): CacheStorage {
    val file = Files.createDirectories(Paths.get("cache")).toFile()
    return FileStorage(file)
}

actual fun getPlatformHttpClient(): HttpClient {
    return HttpClient()
}

actual val screenHeightPixels: Int = Toolkit.getDefaultToolkit().screenSize.height
actual val screenWidthPixels: Int = Toolkit.getDefaultToolkit().screenSize.width
actual val screenDensityDPI: Int = Toolkit.getDefaultToolkit().screenResolution

@Composable
fun MainView() = MainScaffold()

@Preview
@Composable
fun AppPreview() {
    MainScaffold()
}