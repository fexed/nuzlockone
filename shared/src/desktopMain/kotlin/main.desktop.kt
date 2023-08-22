import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import java.util.Locale

actual fun getPlatformName(): String = "Desktop"
actual val language: String?
    get() = Locale.getDefault().language

actual val country: String?
    get() = Locale.getDefault().country
@Composable fun MainView() = MainScaffold()

@Preview
@Composable
fun AppPreview() {
    MainScaffold()
}