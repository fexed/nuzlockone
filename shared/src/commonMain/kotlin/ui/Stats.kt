package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import network.Cache


@Composable
fun StatsCard() {
    val cache = Cache.instance

    Card(modifier = Modifier.padding(32.dp)) {
        Column {
            Row(
                modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(32.dp, 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = "",
                        modifier = Modifier.padding(8.dp)
                    )
                    Text("Creatures")
                }
                if (cache.numberOfPokemons.value != 1) {
                    Text("${cache.numberOfPokemons.value}")
                } else {
                    Box(
                        modifier = Modifier.background(shimmerBrush(cache.numberOfPokemons.value == 1))
                            .width(50.dp).height(20.dp)
                    )
                }
            }
            Divider()
            Row(
                modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(32.dp, 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Place,
                        contentDescription = "",
                        modifier = Modifier.padding(8.dp)
                    )
                    Text("Locations")
                }
                if (cache.numberOfLocations.value != 1) {
                    Text("${cache.numberOfLocations.value}")
                } else {
                    Box(
                        modifier = Modifier.background(shimmerBrush(cache.numberOfLocations.value == 1))
                            .width(50.dp).height(20.dp)
                    )
                }
            }
            Divider()
            Row(
                modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(32.dp, 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "",
                        modifier = Modifier.padding(8.dp)
                    )
                    Text("Games")
                }
                if (cache.numberOfGames.value != 1) {
                    Text("${cache.numberOfGames.value}")
                } else {
                    Box(
                        modifier = Modifier.background(shimmerBrush(cache.numberOfGames.value == 1))
                            .width(50.dp).height(20.dp)
                    )
                }
            }
            Divider()
            Row(
                modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(32.dp, 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "",
                        modifier = Modifier.padding(8.dp)
                    )
                    Text("Items")
                }
                if (cache.numberOfItems.value != 1) {
                    Text("${cache.numberOfItems.value}")
                } else {
                    Box(
                        modifier = Modifier.background(shimmerBrush(cache.numberOfItems.value == 1))
                            .width(50.dp).height(20.dp)
                    )
                }
            }
        }
    }
}