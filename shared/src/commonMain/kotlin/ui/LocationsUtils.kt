package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Location

@Composable
fun LocationRowElement(location: Location) {
    var areDetailsVisible by mutableStateOf(false)

    Card(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp)) {
        Column {
            Row(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(4.dp)) {
                Column {
                    Text("${location.id}", fontSize = 10.sp)
                    Text(location.name, fontSize = 16.sp)
                    Text(location.regionName, fontSize = 14.sp)
                }
            }
        }
    }
}