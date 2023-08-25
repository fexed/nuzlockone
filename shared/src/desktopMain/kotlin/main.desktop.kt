import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.FileStorage
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Locale

actual fun getPlatformName(): String = "Desktop"
actual val language: String?
    get() = Locale.getDefault().language

actual val country: String?
    get() = Locale.getDefault().country

actual fun getCacheFile(): CacheStorage {
    val file = Files.createDirectories(Paths.get("cache")).toFile()
    return FileStorage(file)
}
@Composable fun MainView() = MainScaffold()

@Preview
@Composable
fun AppPreview() {
    MainScaffold()
}