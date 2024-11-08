package com.stevdza_san.sprite.util

import com.stevdza_san.sprite.domain.ScreenCategory

internal fun Int.parseCategory(): ScreenCategory {
    return when (this) {
        in 0..360 -> ScreenCategory.Small
        in 360..600 -> ScreenCategory.Normal
        in 600..800 -> ScreenCategory.Large
        else -> ScreenCategory.Tablet
    }
}