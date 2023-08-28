import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.storage.CacheStorage
import network.Cache

expect fun getPlatformName(): String
expect val language: String?
expect val country: String?
expect val appVersion: String
const val sharedVersion: String = "1.0"
val cache = Cache.instance

expect fun getCacheFile(): CacheStorage
expect fun getPlatformHttpClient(): HttpClient