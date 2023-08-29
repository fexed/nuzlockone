package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import appVersion
import getPlatformName
import sharedVersion

@Composable
fun Settings(paddingValues: PaddingValues) {
    Column(modifier = Modifier.padding(paddingValues)) {
        Text("${getPlatformName()} app version $appVersion", modifier = Modifier.padding(16.dp))
        Divider()
        Text("Shared module version $sharedVersion", modifier = Modifier.padding(16.dp))
    }
}