import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

actual fun getPlatformName(): String = "Desktop"

@Composable fun MainView() = MainPage()

@Preview
@Composable
fun AppPreview() {
    MainPage()
}