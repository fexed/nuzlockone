import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.storage.CacheStorage

expect fun getPlatformName(): String
expect val language: String?
expect val country: String?
expect val appVersion: String
const val sharedVersion: String = "1.0"

expect fun getCacheFile(): CacheStorage
expect fun getPlatformHttpClient(): HttpClient