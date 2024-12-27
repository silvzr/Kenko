package com.looker.kenko.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun normalizeInt(value: Int): String {
    return remember(value) {
        value.toString()
    }
}