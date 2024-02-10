package ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import appVersion
import getPlatformName
import sharedVersion

@Composable
fun Settings(paddingValues: PaddingValues) {
    Column(modifier = Modifier.padding(paddingValues)) {
        MaterialTheme(
            colors = if (isSystemInDarkTheme()) DarkColors else LightColors
        ) {
            Text("Credits", modifier = Modifier.padding(16.dp), fontSize = MaterialTheme.typography.h4.fontSize)
            Text("Made by Fexed", modifier = Modifier.padding(16.dp), fontSize = MaterialTheme.typography.body1.fontSize)

            val annotatedString = buildAnnotatedString {
                pushStringAnnotation(tag = "website", annotation = "https://fexed.github.io/")
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                    append("fexed.github.io")
                }
                pop()
            }

            ClickableText(text = annotatedString, onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "website", start = offset, end = offset).firstOrNull()?.let {

                }
            }, modifier = Modifier.padding(16.dp))

            Text("Infos", modifier = Modifier.padding(16.dp), fontSize = MaterialTheme.typography.h4.fontSize)
            Text("${getPlatformName()} app version $appVersion", modifier = Modifier.padding(16.dp), fontSize = MaterialTheme.typography.body1.fontSize)
            Divider()
            Text("Shared module version $sharedVersion", modifier = Modifier.padding(16.dp), fontSize = MaterialTheme.typography.body1.fontSize)
        }
    }
}