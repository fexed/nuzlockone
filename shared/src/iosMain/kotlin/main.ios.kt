import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun getPlatformName(): String = "iOS"
actual val language: String?
    get() = NSLocale.currentLocale.languageCode

actual val country:String?
    get() = NSLocale.currentLocale.countryCode

fun MainViewController() = ComposeUIViewController { MainScaffold(content = { Tests() }) }