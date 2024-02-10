package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import cache
import com.seiko.imageloader.rememberImagePainter
import data.Item
import kotlinx.coroutines.coroutineScope
import network.PokeApi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Resource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ItemCard(item: Item, isLoading: Boolean = false) {
    val painter = rememberImagePainter(item.imageURL)

    Card(modifier = Modifier.wrapContentSize().padding(8.dp)) {
        Column(
            modifier = Modifier.wrapContentSize().padding(8.dp).background(
                shimmerBrush(showShimmer = isLoading)
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isLoading) {
                Text(item.name)
                Image(
                    painter,
                    null,
                    modifier = Modifier.size(125.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${item.cost}")
                    Spacer(modifier = Modifier.padding(4.dp))
                    Image(painter = painterResource("pokedollar.xml"), contentDescription = null, modifier = Modifier.size(15.dp), colorFilter = ColorFilter.tint(if (!isSystemInDarkTheme()) Color.Black else Color.White))
                }
            } else {
                Box(modifier = Modifier.size(100.dp))
            }
        }
    }
}

@Composable
fun ListAllItems(paddingValues: PaddingValues) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = paddingValues
    ) {
        items(count = cache.numberOfItems.value) {
            var item by remember { mutableStateOf(cache.itemsList[it]) }
            val isLoading = item.name == "Loading..."

            if (isLoading || !item.isValid) {
                LaunchedEffect(true) {
                    coroutineScope {
                        cache.itemsList[it] = try {
                            PokeApi().getItemData(it + 1)
                        } catch (e: Exception) {
                            Item().apply {
                                id = -1
                                name = e.message ?: "Error"
                            }
                        }
                        item = cache.itemsList[it]
                    }
                }
            }

            if (item.isValid) {
                if (!isFiltered(item)) ItemCard(item, isLoading = isLoading)
            } else ItemCard(item, isLoading = true)
        }
    }
}