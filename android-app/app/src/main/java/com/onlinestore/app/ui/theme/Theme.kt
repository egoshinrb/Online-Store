package com.onlinestore.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Green = Color(0xFF2E7D32)

@Composable
fun OnlineStoreTheme(content: @Composable () -> Unit) {
    val scheme = lightColorScheme(
        primary = Green,
        secondary = Green,
        tertiary = Green
    )
    MaterialTheme(colorScheme = scheme, content = content)
}
