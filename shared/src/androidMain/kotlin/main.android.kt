import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import java.util.Locale

actual fun getPlatformName(): String = "Android"
actual val language: String?
    get() = Locale.getDefault().language

actual val country: String?
    get() = Locale.getDefault().country
@Composable fun MainView() = MainScaffold()
