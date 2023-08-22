import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

actual fun getPlatformName(): String = "Desktop"

@Composable fun MainView() = MainScaffold(content = { UITests() })

@Preview
@Composable
fun AppPreview() {
    MainScaffold(content = { UITests() })
}