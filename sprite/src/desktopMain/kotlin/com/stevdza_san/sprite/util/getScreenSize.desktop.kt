package com.stevdza_san.sprite.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
actual fun getScreenWidth(): Dp {
    var size by remember { mutableStateOf(0.dp) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                size = it.size.width.dp
            }
    )
    return size
}

@Composable
actual fun getScreenHeight(): Dp {
    var size by remember { mutableStateOf(0.dp) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                size = layoutCoordinates.size.height.dp
            }
    )
    return size
}