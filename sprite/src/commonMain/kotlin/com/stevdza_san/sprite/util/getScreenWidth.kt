package com.stevdza_san.sprite.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

/**
 * Helper function used to calculate the screen width on each target platform (Android/iOS).
 * */
@Composable
expect fun getScreenWidth(): Dp