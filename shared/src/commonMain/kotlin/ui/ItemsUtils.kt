package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import data.Item
import kotlinx.coroutines.coroutineScope
import network.Cache
import network.PokeApi

@Composable
fun ItemCard(item: Item, isLoading: Boolean = false) {
    val painter = rememberImagePainter(item.imageURL)

    Card(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp)) {
        Row(
            modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp).background(
                shimmerBrush(showShimmer = isLoading)
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isLoading) {
                Image(
                    painter,
                    null,
                    modifier = Modifier.size(25.dp)
                )
                Text(item.name)
                Spacer(
                    Modifier.weight(1f).fillMaxHeight()
                )
                Text("${item.cost}")
            } else {
                Box(modifier = Modifier.size(25.dp))
            }
        }
    }
}

@Composable
fun ListAllItems(paddingValues: PaddingValues) {
    LazyColumn(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = paddingValues
    ) {
        items(count = Cache.instance.numberOfItems.value) {
            var item by remember { mutableStateOf(Cache.instance.itemsList[it]) }
            val isLoading = item.name == "Loading..."

            if (isLoading || !item.isValid) {
                LaunchedEffect(true) {
                    coroutineScope {
                        Cache.instance.itemsList[it] = try {
                            PokeApi().getItemData(it + 1)
                        } catch (e: Exception) {
                            Item().apply {
                                id = -1
                                name = e.message ?: "Error"
                            }
                        }
                        item = Cache.instance.itemsList[it]
                    }
                }
            }

            if (item.isValid) {
                ItemCard(item, isLoading = isLoading)
            } else ItemCard(item, isLoading = true)
        }
    }
}