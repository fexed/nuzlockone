import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

actual fun getPlatformName(): String = "Android"

@Composable fun MainView() = MainScaffold(content = { Tests() })
